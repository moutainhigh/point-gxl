package com.hds.xquark.service.point.operator;

import static com.google.common.base.Preconditions.checkArgument;

import com.hds.xquark.dal.mapper.CommissionRecordMapper;
import com.hds.xquark.dal.mapper.PointRecordMapper;
import com.hds.xquark.dal.model.BasePointCommRecord;
import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.model.CommissionRecord;
import com.hds.xquark.dal.model.GradeCode;
import com.hds.xquark.dal.model.PointRecord;
import com.hds.xquark.dal.model.PointTotal;
import com.hds.xquark.dal.type.CodeNameType;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.PointOperateType;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.service.error.BizException;
import com.hds.xquark.service.error.GlobalErrorCode;
import com.hds.xquark.service.point.PointCommCalResult;
import com.hds.xquark.service.point.PointCommOperationResult;
import com.hds.xquark.service.point.PointCommService;
import com.hds.xquark.service.point.helper.PointCommCalHelper;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

/**
 * @author wangxinhua on 2018/5/21. DESC: 积分修改类
 */
public abstract class BasePointCommOperator {

  private PointRecordMapper pointRecordMapper;

  private CommissionRecordMapper commissionRecordMapper;

  private PointCommService pointCommService;

  /**
   * 积分状态字段
   */
  protected enum PointStatus {
    USABLE,
    FREEZED
  }

  @Autowired
  public void setPointRecordMapper(PointRecordMapper pointRecordMapper) {
    this.pointRecordMapper = pointRecordMapper;
  }

  @Autowired
  public void setCommissionRecordMapper(
      CommissionRecordMapper commissionRecordMapper) {
    this.commissionRecordMapper = commissionRecordMapper;
  }

  @Autowired
  public void setPointCommService(PointCommService pointCommService) {
    this.pointCommService = pointCommService;
  }

  /**
   * 计算积分/德分
   *
   * @param cpId 用户id, 调用前需要确保用户存在
   * @param businessId 业务id
   * @param grade 规则
   * @param pointComms 动态积分/德分
   * @param trancd
   * @return 积分计算结果 {@link PointCommOperationResult}
   */
  public PointCommOperationResult doOperation(Long cpId, String businessId, GradeCode grade,
      PlatformType platform, BigDecimal pointComms,
      PointOperateType operateType, Trancd trancd) {

    checkArgument(grade.getCodeName() == currType(), "积分规则类型不匹配");
    BasePointCommTotal infoBefore = pointCommService
        .loadOrBuildInfo(cpId, operateType.getTotalClazz());

    // 根据公式动态计算积分
    // modified at 2018-06-14 公式功能已被取消, 积分全都动态计算
    dynamicPoints(grade, pointComms);
    // 参数校验
    preCheck(grade);
    // 计算修改后的积分
    PointCommOperatorContext context = new PointCommOperatorContext(cpId, infoBefore, grade,
        businessId,
        platform, operateType, trancd);
    PointCommCalResult calRet = calRet(context);

    BasePointCommTotal infoAfter = calRet.getInfoAfter();
    // 校验积分修改前后数值
    BigDecimal before = infoBefore.getTotal();
    BigDecimal after = infoAfter.getTotal();
    BigDecimal modified = after.subtract(before);

    // 返回封装结果
    PointCommOperationResult ret = new PointCommOperationResult();
    ret.setCpId(cpId);
    ret.setCurrentPoint(modified);
    ret.setGradeId(grade.getId());
    ret.setPlatform(platform);
    ret.setInfoBefore(infoBefore);
    ret.setInfoAfter(infoAfter);
    ret.setRollBacked(calRet.getRollBacked());
    ret.setUsingGradeType(currType());
    ret.setDetailMap(calRet.getDetailMap());
    ret.setTrancd(trancd);
    return ret;
  }

  /**
   * 由具体的子类实现计算积分逻辑
   *
   * @param context @return 积分计算结果
   */
  public abstract PointCommCalResult calRet(PointCommOperatorContext context);

  /**
   * 获取当前的处理类型
   *
   * @return {@link CodeNameType}
   */
  public abstract CodeNameType currType();

  /**
   * 根据积分数及规则公式重新计算规则积分
   *
   * @param grade 积分规则
   * @param points 动态积分值
   */
  private void dynamicPoints(GradeCode grade, BigDecimal points) {
    // 统一使用动态积分
    grade.setPoint(points);
  }

  /**
   * 校验积分规则与历史积分信息
   *
   * @param grade 积分规则
   * @throws BizException 校验有问题排除业务异常
   */
  protected void preCheck(GradeCode grade) {
    // 静态规则积分数量已定
    // 动态规则积分数量在此方法之前已经设置
    // 到这一步如果积分数还没有设置完则抛出异常
    // 部分业务不需要设置积分, 如回退则覆盖此方法
    BigDecimal targetPoints = grade.getPoint();
    if (targetPoints == null || targetPoints.signum() == 0) {
      throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "请指定修改积分数量");
    }
  }

  /**
   * 保存积分记录, 不同的操作对积分记录处理不同因此由不同的子类实现
   *
   * @param bizId 业务id
   * @param grade 积分规则
   * @param calRet 计算结果
   * @return 保存结果
   */
  public boolean saveBackRecord(String bizId, GradeCode grade,
      PointCommOperationResult calRet,
      Class<? extends BasePointCommRecord> clazz) {
    // 保存积分记录
    BasePointCommRecord record = buildRecord(bizId, grade, calRet, calRet.getTrancd(), clazz);
    record.setRollbacked(false);
    return saveRecord(record);
  }

  /**
   * 根据结果构建积分记录
   *
   * @param bizId 业务id
   * @param grade 积分规则
   * @param calRet 计算结果
   * @param clazz 积分记录类型
   * @return 生成的积分记录
   */
  <T extends BasePointCommRecord> T buildRecord(String bizId, GradeCode grade,
      PointCommOperationResult calRet,
      Trancd recordType,
      Class<T> clazz) {
    BasePointCommTotal infoBefore = calRet.getInfoBefore();
    BasePointCommTotal infoAfter = calRet.getInfoAfter();
    PlatformType platform = calRet.getPlatform();
    Long cpId = calRet.getCpId();
    return PointCommCalHelper
        .buildRecord(cpId, bizId, grade, infoBefore, infoAfter, platform, recordType, clazz);
  }

  /**
   * 将明细记录拆分
   */
  <T extends BasePointCommRecord> List<T> buildRecords(String bizId, GradeCode grade,
      PointCommOperationResult calRet,
      Trancd recordType,
      Class<T> clazz) {
    Map<PlatformType, BigDecimal> detailMap = calRet.getDetailMap();
    if (MapUtils.isEmpty(detailMap)) {
      throw new BizException(GlobalErrorCode.UNKNOWN, "没有区分积分平台");
    }
    BasePointCommTotal infoBefore = calRet.getInfoBefore();
    BasePointCommTotal infoAfter = calRet.getInfoAfter();
    Long cpId = calRet.getCpId();
    List<T> ret = new ArrayList<>(detailMap.size());
    for (Entry<PlatformType, BigDecimal> entry : detailMap.entrySet()) {
      PlatformType platform = entry.getKey();
      T record = PointCommCalHelper.buildRecord(cpId, bizId, grade, infoBefore,
          infoAfter, platform, recordType, clazz);
      ret.add(record);
    }
    return ret;
  }

  /**
   * 获取平台对应的积分
   *
   * @param pointTotal 总积分对象
   * @param platform 积分平台
   * @param status 积分状态(前缀)
   * @return 获取到的积分
   */
  protected Long getPlatFormPoint(PointTotal pointTotal,
      PlatformType platform, PointStatus status) {
    String methodName = "get" + status.name().toLowerCase()
        + "Point" + platform.getFullName();
    Method method;
    try {
      method = pointTotal.getClass().getDeclaredMethod(methodName, PointTotal.class);
    } catch (NoSuchMethodException e) {
      throw new BizException(GlobalErrorCode.INTERNAL_ERROR, e);
    }
    return (Long) ReflectionUtils.invokeMethod(method, pointTotal);
  }

  /**
   * 设置平台对应的积分
   *
   * @param pointTotal 总积分对象
   * @param newPoint 新设置的积分
   * @param platform 积分平台
   * @param status 积分状态(前缀)
   */
  protected void setPlatFormPoint(PointTotal pointTotal, Long newPoint,
      PlatformType platform, PointStatus status) {
    String methodName = getMethodName("set", status, platform);
    Method method;
    try {
      method = pointTotal.getClass().getDeclaredMethod(methodName, PointTotal.class);
    } catch (NoSuchMethodException e) {
      throw new BizException(GlobalErrorCode.INTERNAL_ERROR, e);
    }
    ReflectionUtils.invokeMethod(method, pointTotal, newPoint);
  }

  /**
   * 根据平台、前缀拼接方法名
   *
   * @param prefix 前缀名 set 或 get
   * @param status 状态 {@link PointStatus}
   * @param platform 平台 {@link PlatformType}
   * @return 拼接后的字符串
   */
  private String getMethodName(String prefix, PointStatus status, PlatformType platform) {
    String statusStr = status.name();
    return prefix + statusStr.substring(0, 1) + statusStr.substring(1)
        .toLowerCase() + "Point" + platform.getFullName();
  }

  /**
   * 保存积分/德分记录
   */
  <T extends BasePointCommRecord> boolean saveRecord(T record) {
    if (record instanceof PointRecord) {
      return pointRecordMapper.insert((PointRecord) record) > 0;
    } else if (record instanceof CommissionRecord) {
      return commissionRecordMapper.insert((CommissionRecord) record) > 0;
    } else {
      throw new BizException(GlobalErrorCode.POINT_NOT_SUPPORT);
    }
  }

  /**
   * 更新积分/德分记录
   */
  <T extends BasePointCommRecord> boolean updateRecord(T record) {
    if (record instanceof PointRecord) {
      return pointRecordMapper.updateByPrimaryKeySelective((PointRecord) record) > 0;
    } else if (record instanceof CommissionRecord) {
      return commissionRecordMapper.updateByPrimaryKeySelective((CommissionRecord) record) > 0;
    } else {
      throw new BizException(GlobalErrorCode.POINT_NOT_SUPPORT);
    }
  }

}
