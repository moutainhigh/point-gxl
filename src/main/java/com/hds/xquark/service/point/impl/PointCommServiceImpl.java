package com.hds.xquark.service.point.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import com.hds.xquark.dal.constrant.GradeCodeConstrants;
import com.hds.xquark.dal.constrant.PointConstrants;
import com.hds.xquark.dal.mapper.CommissionRecordMapper;
import com.hds.xquark.dal.mapper.CommissionTotalMapper;
import com.hds.xquark.dal.mapper.PointRecordMapper;
import com.hds.xquark.dal.mapper.PointTotalMapper;
import com.hds.xquark.dal.model.BasePointCommRecord;
import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.model.CommissionRecord;
import com.hds.xquark.dal.model.CommissionTotal;
import com.hds.xquark.dal.model.GradeCode;
import com.hds.xquark.dal.model.PointRecord;
import com.hds.xquark.dal.model.PointTotal;
import com.hds.xquark.dal.type.CodeNameType;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.PointOperateType;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.dal.vo.CommissionRecordVO;
import com.hds.xquark.dal.vo.PointRecordVO;
import com.hds.xquark.service.error.BizException;
import com.hds.xquark.service.error.GlobalErrorCode;
import com.hds.xquark.service.point.PointCommOperationResult;
import com.hds.xquark.service.point.PointCommService;
import com.hds.xquark.service.point.PointGradeService;
import com.hds.xquark.service.point.operator.BasePointCommOperator;
import com.hds.xquark.service.point.operator.PointOperatorFactory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangxinhua on 2018/5/21. DESC:
 */
@Service
public class PointCommServiceImpl implements PointCommService {

  private PointGradeService pointGradeService;

  private PointTotalMapper pointTotalMapper;

  private CommissionTotalMapper commissionTotalMapper;

  private PointRecordMapper pointRecordMapper;

  private CommissionRecordMapper commissionRecordMapper;

  @Autowired
  public void setPointGradeService(PointGradeService pointGradeService) {
    this.pointGradeService = pointGradeService;
  }

  @Autowired
  public void setPointTotalMapper(PointTotalMapper pointTotalMapper) {
    this.pointTotalMapper = pointTotalMapper;
  }

  @Autowired
  public void setCommissionTotalMapper(CommissionTotalMapper commissionTotalMapper) {
    this.commissionTotalMapper = commissionTotalMapper;
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

  /**
   * 修改德分
   *
   * @param cpId 用户id
   * @param bizId 业务id
   * @param categoryId 组id
   * @param status 状态
   * @param platform 平台
   * @param points 修改积分
   * @param trancd 类型
   * @return 修改结果
   */
  @Override
  public PointCommOperationResult modifyPoint(Long cpId, String bizId,
      Integer categoryId, Integer status, PlatformType platform, BigDecimal points, Trancd trancd) {
    if (categoryId != PointConstrants.POINT_CATEGORY) {
      throw new BizException(GlobalErrorCode.POINT_WRONG_OPERATION);
    }
    return modifyPointComm(cpId, bizId, categoryId, status, platform, points,
        PointOperateType.POINT,
        trancd);
  }

  /**
   * 修改积分
   *
   * @param cpId 用户id
   * @param bizId 业务id
   * @param categoryId 组id
   * @param status 状态
   * @param platform 平台
   * @param commission 修改积分
   * @param trancd 类型
   * @return 修改结果
   */
  @Override
  public PointCommOperationResult modifyCommission(Long cpId, String bizId,
      Integer categoryId, Integer status, PlatformType platform, BigDecimal commission,
      Trancd trancd) {
    if (categoryId != PointConstrants.COMMISSION_CATEGORY) {
      throw new BizException(GlobalErrorCode.POINT_WRONG_OPERATION);
    }
    return modifyPointComm(cpId, bizId, categoryId, status, platform, commission,
        PointOperateType.COMMISSION, trancd);
  }

  @Override
  public PointCommOperationResult modifyPoint(Long cpId, String bizId,
      String funcCode, PlatformType platform, BigDecimal points,
      Trancd trancd) {
    return modifyPointComm(cpId, bizId, funcCode, platform, points, PointOperateType.POINT, trancd);
  }

  @Override
  public PointCommOperationResult modifyCommission(Long cpId, String bizId,
      String funcCode, PlatformType platform, BigDecimal commission,
      Trancd trancd) {
    return modifyPointComm(cpId, bizId, funcCode, platform, commission,
        PointOperateType.COMMISSION, trancd);
  }

  private PointCommOperationResult modifyPointComm(Long cpId, String bizId, GradeCode grade,
      PlatformType platform, BigDecimal points, PointOperateType operateType,
      Trancd trancd) {
    checkNotNull(grade, "德分规则不能为空");
    CodeNameType gradeType = grade.getCodeName();
    checkNotNull(gradeType, "德分规则类型未指定");

    BasePointCommOperator operator = PointOperatorFactory.getOperator(gradeType);
    // 根据不同操作计算积分结果
    PointCommOperationResult ret = operator.doOperation(cpId, bizId, grade,
        platform, points, operateType, trancd);
    saveRet(bizId, grade, ret, operator, operateType.getRecordClazz());
    return ret;
  }

  private PointCommOperationResult modifyPointComm(Long cpId, String bizId, Integer categoryId,
      Integer status, PlatformType platform, BigDecimal points, PointOperateType operateType,
      Trancd trancd) {
    GradeCode grade = pointGradeService.loadByCategoryAndStatus(categoryId, status);
    if (grade == null) {
      throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "规则代码无效");
    }
    return modifyPointComm(cpId, bizId, grade, platform, points, operateType, trancd);
  }

  private PointCommOperationResult modifyPointComm(Long cpId, String bizId,
      String functionCode, PlatformType platform, BigDecimal points,
      PointOperateType operateType, Trancd trancd) {
    GradeCode grade = pointGradeService.loadByFunctionCode(functionCode);
    if (grade == null) {
      throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "规则代码无效");
    }
    return modifyPointComm(cpId, bizId, grade, platform, points, operateType, trancd);
  }

  /**
   * 保存或更新用户积分信息
   *
   * @param info 积分信息VO
   * @return 保存结果
   */
  @Override
  public boolean saveOrUpdate(BasePointCommTotal info) {
    boolean ret;
    if (info.getId() == null) {
      ret = saveTotal(info);
    } else {
      ret = updateTotal(info);
    }
    return ret;
  }

  @Override
  public PointTotal loadByCpId(Long cpId) {
    return pointTotalMapper.selectByCpId(cpId);
  }

  @Override
  public CommissionTotal loadCommByCpId(Long cpId) {
    return commissionTotalMapper.selectByCpId(cpId);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends BasePointCommTotal> T loadOrBuildInfo(Long cpId, Class<T> clazz) {
    T ret;
    if (clazz == PointTotal.class) {
      ret = (T) pointTotalMapper.selectByCpId(cpId);
    } else if (clazz == CommissionTotal.class) {
      ret = (T) commissionTotalMapper.selectByCpId(cpId);
    } else {
      throw new BizException(GlobalErrorCode.UNKNOWN, "积分形式不支持");
    }
    if (ret == null) {
      ret = BasePointCommTotal.emptyInfo(cpId, clazz);
    }
    return ret;
  }

  /**
   * 保存积分或德分 或其他乱七八糟分, 如果还有什么鬼分用抽象类去实现
   *
   * @param total 积分或德分
   * @return 保存结果
   */
  private boolean saveTotal(BasePointCommTotal total) {
    if (total instanceof PointTotal) {
      return pointTotalMapper.insert((PointTotal) total) > 0;
    } else if (total instanceof CommissionTotal) {
      return commissionTotalMapper.insert((CommissionTotal) total) > 0;
    } else {
      throw new BizException(GlobalErrorCode.POINT_NOT_SUPPORT);
    }
  }

  /**
   * 更新积分或德分 或其他乱七八糟分, 如果还有什么鬼分用抽象类去实现
   *
   * @param total 积分或德分
   * @return 更新结果
   */
  private boolean updateTotal(BasePointCommTotal total) {
    if (total instanceof PointTotal) {
      return pointTotalMapper.updateByPrimaryKeySelective((PointTotal) total) > 0;
    } else if (total instanceof CommissionTotal) {
      return commissionTotalMapper.updateByPrimaryKeySelective((CommissionTotal) total) > 0;
    } else {
      throw new BizException(GlobalErrorCode.POINT_NOT_SUPPORT);
    }
  }

  /**
   * 统一保存积分修改结果
   *
   * @param operationResult 积分计算结果
   * @param operator 规则实现类
   * @param clazz 积分记录class类型
   */
  // TODO 事务问题
  @Transactional(rollbackFor = Exception.class)
  void saveRet(String bizId, GradeCode grade, PointCommOperationResult operationResult,
      BasePointCommOperator operator,
      Class<? extends BasePointCommRecord> clazz) {

    // 保存积分记录
    List<? extends BasePointCommRecord> records = operator
        .saveBackRecord(bizId, grade, operationResult, clazz);

    // 更新或保存用户积分信息
    BasePointCommTotal infoAfter = operationResult.getInfoAfter();
    boolean ret = saveOrUpdate(infoAfter);
    if (!ret) {
      throw new BizException(GlobalErrorCode.INTERNAL_ERROR, "内部错误, 请稍后再试");
    }
    operationResult.setCurrRecords(records);
    // 保存积分记录积分记录
  }

  /**
   * 查询德分记录
   */
  @Override
  public Map<String, Object> listPointRecords(Long cpId, Integer code, Integer offset,
      Integer size) {
    List<PointRecordVO> list = pointRecordMapper.listVO(cpId, code, offset, size);
    Long total = pointRecordMapper.count(cpId, code);
    return ImmutableMap.of("list", list, "total", total);
  }

  /**
   * 查询积分记录
   */
  @Override
  public Map<String, Object> listCommissionRecords(Long cpId, Integer code, Integer offset,
      Integer size) {
    List<CommissionRecordVO> list = commissionRecordMapper.listVO(cpId, code, offset, size);
    Long total = commissionRecordMapper.count(cpId, code);
    return ImmutableMap.of("list", list, "total", total);
  }

  /**
   * 发放佣金
   */
  @Override
  public int releaseCommission() {
    List<CommissionRecord> unfreezedPoints = listUnFreezedRecord(CommissionRecord.class);
    if (CollectionUtils.isEmpty(unfreezedPoints)) {
      return 0;
    }
    for (CommissionRecord record : unfreezedPoints) {
      BigDecimal point = record.getCurrentFreezed();
      Long cpId = record.getCpId();
      String bizId = record.getBusinessId();
      PlatformType platform = record.getPlatForm();
      // 解冻原记录
      PointCommOperationResult ret = modifyPointComm(cpId, bizId,
          GradeCodeConstrants.RELEASE_COMMISSION_CODE, platform, point,
          PointOperateType.COMMISSION, record.getTrancd());
      @SuppressWarnings("unchecked")
      List<CommissionRecord> currRecords = (List<CommissionRecord>) ret.getCurrRecords();
      if (CollectionUtils.isEmpty(currRecords) || currRecords.size() != 1) {
        throw new BizException(GlobalErrorCode.UNKNOWN, "积分解除冻结失败");
      }
      CommissionRecord record1 = currRecords.get(0);
      record.setUnFreezeId(record1.getId());
      commissionRecordMapper.updateByPrimaryKeySelective(record);
    }
    return unfreezedPoints.size();
  }

  /**
   * 发放德分
   */
  @Override
  public int releasePoints() {
    List<PointRecord> unfreezedPoints = listUnFreezedRecord(PointRecord.class);
    if (CollectionUtils.isEmpty(unfreezedPoints)) {
      return 0;
    }
    for (PointRecord record : unfreezedPoints) {
      BigDecimal point = record.getCurrentFreezed();
      Long cpId = record.getCpId();
      String bizId = record.getBusinessId();
      PlatformType platform = record.getPlatForm();
      // 解冻原记录
      PointCommOperationResult ret = modifyPointComm(cpId, bizId,
          GradeCodeConstrants.RELEASE_POINT_CODE, platform, point,
          PointOperateType.POINT, record.getTrancd());
      @SuppressWarnings("unchecked")
      List<PointRecord> currRecords = (List<PointRecord>) ret.getCurrRecords();
      if (CollectionUtils.isEmpty(currRecords) || currRecords.size() != 1) {
        throw new BizException(GlobalErrorCode.UNKNOWN, "积分解除冻结失败");
      }
      PointRecord record1 = currRecords.get(0);
      record.setUnFreezeId(record1.getId());
      pointRecordMapper.updateByPrimaryKeySelective(record);
    }
    return unfreezedPoints.size();
  }

  @SuppressWarnings("unchecked")
  private <T extends BasePointCommRecord> List<T> listUnFreezedRecord(Class<T> clazz) {
    if (clazz == PointRecord.class) {
      return (List<T>) pointRecordMapper.listUnFreezedRecord();
    } else if (clazz == CommissionRecord.class) {
      return (List<T>) commissionRecordMapper.listUnFreezedRecord();
    } else {
      throw new BizException(GlobalErrorCode.POINT_NOT_SUPPORT);
    }
  }

}
