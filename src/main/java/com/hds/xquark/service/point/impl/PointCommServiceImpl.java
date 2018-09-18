package com.hds.xquark.service.point.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.hds.xquark.dal.type.Trancd.DEPOSIT_C;
import static com.hds.xquark.dal.type.Trancd.DEPOSIT_P;
import static com.hds.xquark.dal.type.Trancd.MIGRATE_C;
import static com.hds.xquark.dal.type.Trancd.MIGRATE_P;
import static com.hds.xquark.dal.type.Trancd.REWARD_C;
import static com.hds.xquark.dal.type.Trancd.REWARD_P;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.hds.xquark.dal.constrant.GradeCodeConstrants;
import com.hds.xquark.dal.constrant.PointConstrants;
import com.hds.xquark.dal.mapper.CommissionRecordMapper;
import com.hds.xquark.dal.mapper.CommissionSuspendingAsstMapper;
import com.hds.xquark.dal.mapper.CommissionTotalAuditMapper;
import com.hds.xquark.dal.mapper.CommissionTotalMapper;
import com.hds.xquark.dal.mapper.CustomerWithdrawalMapper;
import com.hds.xquark.dal.mapper.PointRecordMapper;
import com.hds.xquark.dal.mapper.PointSuspendingAsstMapper;
import com.hds.xquark.dal.mapper.PointTotalAuditMapper;
import com.hds.xquark.dal.mapper.PointTotalMapper;
import com.hds.xquark.dal.model.BasePointCommAsst;
import com.hds.xquark.dal.model.BasePointCommRecord;
import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.model.CommissionRecord;
import com.hds.xquark.dal.model.CommissionSuspendingAsst;
import com.hds.xquark.dal.model.CommissionTotal;
import com.hds.xquark.dal.model.CommissionTotalAudit;
import com.hds.xquark.dal.model.CustomerWithdrawal;
import com.hds.xquark.dal.model.GradeCode;
import com.hds.xquark.dal.model.PointRecord;
import com.hds.xquark.dal.model.PointSuspendingAsst;
import com.hds.xquark.dal.model.PointTotal;
import com.hds.xquark.dal.model.PointTotalAudit;
import com.hds.xquark.dal.type.AuditType;
import com.hds.xquark.dal.type.CodeNameType;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.PointOperateType;
import com.hds.xquark.dal.type.TotalAuditType;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.dal.util.Transformer;
import com.hds.xquark.dal.vo.CommissionRecordVO;
import com.hds.xquark.dal.vo.CommissionWithdrawVO;
import com.hds.xquark.dal.vo.PointRecordVO;
import com.hds.xquark.service.error.BizException;
import com.hds.xquark.service.error.GlobalErrorCode;
import com.hds.xquark.service.point.PointCommOperationResult;
import com.hds.xquark.service.point.PointCommService;
import com.hds.xquark.service.point.PointGradeService;
import com.hds.xquark.service.point.operator.BasePointCommOperator;
import com.hds.xquark.service.point.operator.PointOperatorFactory;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangxinhua on 2018/5/21. DESC:
 */
@Service("PointCommService")
public class PointCommServiceImpl implements PointCommService {
  private final static Logger LOGGER = Logger.getLogger(PointCommServiceImpl.class);

  private PointGradeService pointGradeService;

  private PointTotalMapper pointTotalMapper;

  private CommissionTotalMapper commissionTotalMapper;

  private PointRecordMapper pointRecordMapper;

  private CommissionRecordMapper commissionRecordMapper;

  private PointTotalAuditMapper pointTotalAuditMapper;

  private CommissionTotalAuditMapper commissionTotalAuditMapper;

  private CustomerWithdrawalMapper customerWithdrawalMapper;

  private PointSuspendingAsstMapper pointSuspendingAsstMapper;

  private CommissionSuspendingAsstMapper commissionSuspendingAsstMapper;

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
  public void setPointTotalAuditMapper(
      PointTotalAuditMapper pointTotalAuditMapper) {
    this.pointTotalAuditMapper = pointTotalAuditMapper;
  }

  @Autowired
  public void setCommissionTotalAuditMapper(
      CommissionTotalAuditMapper commissionTotalAuditMapper) {
    this.commissionTotalAuditMapper = commissionTotalAuditMapper;
  }

  @Autowired
  public void setCommissionRecordMapper(
      CommissionRecordMapper commissionRecordMapper) {
    this.commissionRecordMapper = commissionRecordMapper;
  }

  @Autowired
  public void setCustomerWithdrawalMapper(
      CustomerWithdrawalMapper customerWithdrawalMapper) {
    this.customerWithdrawalMapper = customerWithdrawalMapper;
  }

  @Autowired
  public void setPointSuspendingAsstMapper(
      PointSuspendingAsstMapper pointSuspendingAsstMapper) {
    this.pointSuspendingAsstMapper = pointSuspendingAsstMapper;
  }

  @Autowired
  public void setCommissionSuspendingAsstMapper(
      CommissionSuspendingAsstMapper commissionSuspendingAsstMapper) {
    this.commissionSuspendingAsstMapper = commissionSuspendingAsstMapper;
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
      Integer categoryId, Integer status, PlatformType platform, BigDecimal points, Trancd trancd,
      TotalAuditType auditType) {
    if (categoryId != PointConstrants.POINT_CATEGORY) {
      throw new BizException(GlobalErrorCode.POINT_WRONG_OPERATION);
    }
    return modifyPointComm(cpId, bizId, categoryId, status, platform, points,
        PointOperateType.POINT, trancd, auditType);
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
      Trancd trancd, TotalAuditType auditType) {
    if (categoryId != PointConstrants.COMMISSION_CATEGORY) {
      throw new BizException(GlobalErrorCode.POINT_WRONG_OPERATION);
    }
    return modifyPointComm(cpId, bizId, categoryId, status, platform, commission,
        PointOperateType.COMMISSION, trancd, auditType);
  }

  @Override
  public PointCommOperationResult modifyPoint(Long cpId, String bizId,
      String funcCode, PlatformType platform, BigDecimal points,
      Trancd trancd, TotalAuditType auditType) {
    return modifyPointComm(cpId, bizId, funcCode, platform, points,
        PointOperateType.POINT, trancd, auditType);
  }

  @Override
  public PointCommOperationResult modifyCommission(Long cpId, String bizId,
      String funcCode, PlatformType platform, BigDecimal commission,
      Trancd trancd, TotalAuditType auditType) {
    return modifyPointComm(cpId, bizId, funcCode, platform, commission,
        PointOperateType.COMMISSION, trancd, auditType);
  }

  private PointCommOperationResult modifyPointComm(Long cpId, String bizId, GradeCode grade,
      PlatformType platform, BigDecimal points, PointOperateType operateType,
      Trancd trancd, TotalAuditType auditType) {
    checkNotNull(grade, "规则不能为空");
    CodeNameType gradeType = grade.getCodeName();
    checkNotNull(gradeType, "规则类型未指定");

    BasePointCommOperator operator = PointOperatorFactory.getOperator(gradeType);
    // 根据不同操作计算积分结果
    PointCommOperationResult ret = operator.doOperation(cpId, bizId, grade,
        platform, points, operateType, trancd);
    saveRet(bizId, grade, ret, operator, operateType.getRecordClazz(), auditType);
    return ret;
  }

  private PointCommOperationResult modifyPointComm(Long cpId, String bizId, Integer categoryId,
      Integer status, PlatformType platform, BigDecimal points, PointOperateType operateType,
      Trancd trancd, TotalAuditType auditType) {
    GradeCode grade = pointGradeService.loadByCategoryAndStatus(categoryId, status);
    if (grade == null) {
      throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "规则代码无效");
    }
    return modifyPointComm(cpId, bizId, grade, platform, points, operateType,
        trancd, auditType);
  }

  private PointCommOperationResult modifyPointComm(Long cpId, String bizId,
      String functionCode, PlatformType platform, BigDecimal points,
      PointOperateType operateType, Trancd trancd, TotalAuditType auditType) {
    GradeCode grade = pointGradeService.loadByFunctionCode(functionCode);
    if (grade == null) {
      throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "规则代码无效");
    }
    return modifyPointComm(cpId, bizId, grade, platform, points,
        operateType, trancd, auditType);
  }

  /**
   * 保存或更新用户积分信息
   *
   * @param info 积分信息VO
   * @return 保存结果
   */
  @Override
  public boolean saveOrUpdate(BasePointCommTotal info,
      TotalAuditType auditType) {
    boolean ret;
    if (info.getId() == null) {
      ret = saveTotal(info, auditType);
    } else {
      ret = updateTotal(info, auditType);
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
   * 返回指定grade的积分\德分总额, 保留两位小数, 若查询不到则返回0
   */
  @Override
  public <T extends BasePointCommTotal> BigDecimal sumTotal(String gradeCode, Long cpId,
      Trancd trancd, Class<T> clazz) {
    // 也许在将来需要支持
    if (clazz == PointTotal.class) {
      throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "暂不支持查询德分总数");
    }
    BigDecimal dbVal = commissionRecordMapper.sumTotal(gradeCode, cpId, trancd);
    return Optional.fromNullable(dbVal).transform(new Function<BigDecimal, BigDecimal>() {
      @Override
      public BigDecimal apply(BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_EVEN).abs();
      }
    }).or(BigDecimal.ZERO);
  }

  /**
   * 保存积分或德分 或其他乱七八糟分, 如果还有什么鬼分用抽象类去实现
   *
   * @param total 积分或德分
   * @return 保存结果
   */
  @Override
  public boolean saveTotal(BasePointCommTotal total,
      TotalAuditType auditType) {
    if (total instanceof PointTotal) {
      PointTotal pt = (PointTotal) total;
      insertPointAudit(pt, auditType, AuditType.INSERT);
      return pointTotalMapper.insert(pt) > 0;
    } else if (total instanceof CommissionTotal) {
      CommissionTotal ct = (CommissionTotal) total;
      insertCommissionAudit(ct, auditType, AuditType.INSERT);
      return commissionTotalMapper.insert(ct) > 0;
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
  @Override
  public boolean updateTotal(BasePointCommTotal total,
      TotalAuditType auditType) {
    if (total instanceof PointTotal) {
      PointTotal pt = (PointTotal) total;
      insertPointAudit(pt, auditType, AuditType.UPDATE);
      return pointTotalMapper.updateByPrimaryKeySelective(pt) > 0;
    } else if (total instanceof CommissionTotal) {
      CommissionTotal ct = (CommissionTotal) total;
      insertCommissionAudit(ct, auditType, AuditType.UPDATE);
      return commissionTotalMapper.updateByPrimaryKeySelective(ct) > 0;
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
      Class<? extends BasePointCommRecord> clazz, TotalAuditType auditType) {

    // 保存积分记录
    List<? extends BasePointCommRecord> records = operator
        .saveBackRecord(bizId, grade, operationResult, clazz);

    // 更新或保存用户积分信息
    BasePointCommTotal infoAfter = operationResult.getInfoAfter();
    boolean ret = saveOrUpdate(infoAfter, auditType);
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
  public Map<String, Object> listPointRecords(Long cpId, Integer source, Integer offset,
      Integer size) {
    List<PointRecordVO> list = pointRecordMapper.listVOAsst(cpId, source, offset, size);
    Long total = pointRecordMapper.count(cpId, source);
    return ImmutableMap.of("list", list, "total", total);
  }

  /**
   * 查询积分记录
   */
  @Override
  public Map<String, Object> listCommissionRecords(Long cpId, Integer source, Integer offset,
      Integer size) {
    List<CommissionRecordVO> list = commissionRecordMapper.listVO(cpId, source, offset, size);
    Long total = commissionRecordMapper.count(cpId, source);
    return ImmutableMap.of("list", list, "total", total);
  }

  /**
   * 发放佣金
   */
  @Override
  public int releaseCommission(TotalAuditType auditType) {
    List<CommissionRecord> unfreezedPoints = listUnFreezedRecord(CommissionRecord.class);
    if (CollectionUtils.isEmpty(unfreezedPoints)) {
      return 0;
    }
    int total = unfreezedPoints.size();
    LOGGER.info(MessageFormat.format("=== 开始解冻积分 ===, 共 {0} 条", total));
    int failed = 0;
    for (CommissionRecord record : unfreezedPoints) {
      BigDecimal point = record.getCurrentFreezed();
      Long cpId = record.getCpId();
      String bizId = record.getBusinessId();
      PlatformType platform = record.getPlatForm();
      LOGGER.info(MessageFormat.format("cpId: {0} 开始解冻积分记录 {1}, 待解冻 {2}", cpId, record.getId(),
          record.getCurrentFreezed()));
      // 解冻原记录
      PointCommOperationResult ret;
      try {
        ret = modifyPointComm(cpId, bizId,
            GradeCodeConstrants.RELEASE_COMMISSION_CODE, platform, point,
            PointOperateType.COMMISSION, record.getTrancd(), auditType);
      } catch (Exception e) {
        LOGGER.error(MessageFormat.format("cpId: {0} 积分记录 id: {1} 解冻失败", cpId, record.getId()), e);
        failed++;
        continue;
      }
      @SuppressWarnings("unchecked")
      List<CommissionRecord> currRecords = (List<CommissionRecord>) ret.getCurrRecords();
      if (CollectionUtils.isEmpty(currRecords) || currRecords.size() != 1) {
        LOGGER
            .error(MessageFormat.format("cpId: {0} 没有找到解冻积分 id: {1} 对应的原记录", cpId, record.getId()));
        failed++;
        continue;
      }
      CommissionRecord record1 = currRecords.get(0);
      record.setUnFreezeId(record1.getId());
      commissionRecordMapper.updateByPrimaryKeySelective(record);
      LOGGER.info(MessageFormat.format("cpId: {0} 积分记录 {1} 解冻完毕", cpId, record.getId()));
    }
    LOGGER.info(MessageFormat.format("=== 积分解冻完毕, 处理 {0} 条, 失败 {1} 条 ===", total, failed));
    return total - failed;
  }

  /**
   * 发放德分
   */
  @Override
  public int releasePoints(TotalAuditType auditType) {
    List<PointRecord> unfreezedPoints = listUnFreezedRecord(PointRecord.class);
    int total = unfreezedPoints.size();
    LOGGER.info(MessageFormat.format("=== 开始解冻德分 ===, 共 {0} 条", total));
    int failed = 0;
    if (CollectionUtils.isEmpty(unfreezedPoints)) {
      return 0;
    }
    for (PointRecord record : unfreezedPoints) {
      BigDecimal point = record.getCurrentFreezed();
      Long cpId = record.getCpId();
      String bizId = record.getBusinessId();
      PlatformType platform = record.getPlatForm();
      // 解冻原记录
      PointCommOperationResult ret;
      LOGGER.info(MessageFormat.format("cpId: {0} 开始解冻德分记录 {1}, 待解冻 {2}", cpId, record.getId(),
          record.getCurrentFreezed()));
      try {
        ret = modifyPointComm(cpId, bizId,
            GradeCodeConstrants.RELEASE_POINT_CODE, platform, point,
            PointOperateType.POINT, record.getTrancd(), auditType);
      } catch (Exception e) {
        LOGGER.error(MessageFormat.format("cpId: {0} 德分记录 id: {1} 解冻失败", cpId, record.getId()), e);
        failed++;
        continue;
      }
      @SuppressWarnings("unchecked")
      List<PointRecord> currRecords = (List<PointRecord>) ret.getCurrRecords();
      if (CollectionUtils.isEmpty(currRecords) || currRecords.size() != 1) {
        LOGGER
            .error(MessageFormat.format("cpId: {0} 没有找到解冻德分 id: {1} 对应的原记录", cpId, record.getId()));
        failed++;
        continue;
      }
      PointRecord record1 = currRecords.get(0);
      record.setUnFreezeId(record1.getId());
      pointRecordMapper.updateByPrimaryKeySelective(record);
      LOGGER.info(MessageFormat.format("cpId: {0} 德分记录 {1} 解冻完毕", cpId, record.getId()));
    }
    LOGGER.info(MessageFormat.format("=== 德分解冻完毕, 处理 {0} 条, 失败 {1} 条 ===", total, failed));
    return total - failed;
  }

  @Override
  public void grantPointWithProcedure(Long cpId, PlatformType platform, BigDecimal val,
      Trancd trancd) {
    checkArgument(trancd == REWARD_P || trancd == DEPOSIT_P || trancd == MIGRATE_P,
        "新增德分Trancd错误");
    pointTotalMapper.grantWithProcedure(cpId, platform.getCode(), val, trancd.name(),
        StringUtils.substringBefore(trancd.name().toLowerCase(), "_"));
  }

  @Override
  public void grantCommissionWithProcedure(Long cpId, PlatformType platform, BigDecimal val,
      Trancd trancd) {
    checkArgument(trancd == REWARD_C || trancd == DEPOSIT_C || trancd == MIGRATE_C,
        "新增积分Trancd错误");
    commissionTotalMapper.grantWithProcedure(cpId, platform.getCode(), val, trancd.name(),
        StringUtils.substringBefore(trancd.name().toLowerCase(), "_"));
  }

  @Override
  public List<CommissionRecord> listRecordByTime(Date start, Date end, String grade,
      Integer source) {
    return commissionRecordMapper.listByTimeRange(start, end, grade, source);
  }

  /**
   * 从传入日期开始往前迁移一个月, 往前推到一个月的零点
   *
   * @param from 当前日期
   * @param platform platform
   */
  @Override
  @Transactional
  public int translateCommSuspendingToWithdraw(Date from,
      PlatformType platform) {
    DateTime jobStart;
    // viviLife 一周提现一次
    if (platform == PlatformType.V) {
      jobStart = new DateTime(from)
          .minusWeeks(1).withTimeAtStartOfDay();
    } else {
      jobStart = new DateTime(from)
          .minusMonths(1).withTimeAtStartOfDay();
    }
    return translateCommSuspendingToWithdraw(jobStart.toDate(), from, platform);
  }

  /**
   * 将指定日期的积分提现记录迁移到withdraw表
   *
   * @param start 开始日期
   * @param end 结束日期
   * @param platform 迁移平台
   * @return 执行结果
   */
  @Override
  @Transactional
  public int translateCommSuspendingToWithdraw(Date start, Date end,
      PlatformType platform) {
    List<CommissionRecord> commissionRecords = listRecordByTime(start, end,
        GradeCodeConstrants.WITH_DRAW_COMMISSION_CODE, platform.getCode());
    LOGGER.info(String
        .format("开始迁移 %s 提现记录: %s -> %s, 共 %d 条",
            platform.getFullName(),
            DateFormatUtils.format(start, "yyyy-MM-dd HH:mm:ss"),
            DateFormatUtils.format(end, "yyyy-MM-dd HH:mm:ss"), commissionRecords.size()));
    int effected = 0;
    if (CollectionUtils.isEmpty(commissionRecords)) {
      return effected;
    }
    for (CommissionRecord record : commissionRecords) {
      Long id = record.getId();
      boolean withdrawExists = customerWithdrawalMapper.isSuspendingExists(id);
      if (!withdrawExists) {
        CustomerWithdrawal withdrawal = new CustomerWithdrawal();
        withdrawal.setCommsuspendingId(id);
        withdrawal.setCpId(record.getCpId());
        withdrawal.setAmount(record.getCurrent().abs());
        String dateFormat = platform == PlatformType.V ?
            "yyyyMMdd" : "yyyyMM";
        // viviLife 记录到天
        withdrawal.setProcessingMonth(Integer.parseInt(DateFormatUtils.format(end, dateFormat)));
        withdrawal.setSource(record.getSource());
        withdrawal.setWithdrawDate(record.getCreatedAt());
        try {
          customerWithdrawalMapper.insert(withdrawal);
        } catch (Exception e) {
          LOGGER.error("提现记录 " + id + " 迁移到提现表失败", e);
          continue;
        }
        effected++;
      } else {
        LOGGER.info("提现记录 [" + id + "] 已处理, 跳过处理");
      }
    }
    LOGGER.info(String.format("积分记录迁移完毕, 成功 %d 条", effected));
    return effected;
  }

  /**
   * 查询提现记录
   *
   * @param orderMonth 月份，格式为201809
   * @param source 平台, 若为空则为全平台
   * @return 查询结果
   */
  @Override
  public List<CommissionWithdrawVO> listWithdrawVO(Integer orderMonth, PlatformType source) {
    return customerWithdrawalMapper.listWithdraw(orderMonth, Optional.fromNullable(source)
        .transform(PlatformType.GET_CODE_FUNC).orNull());
  }

  /**
   * 查询中行提现记录
   *
   * @param orderMonth 月份，格式为201809
   * @param source 平台, 若为空则为全平台
   * @return 查询结果
   */
  @Override
  public List<CommissionWithdrawVO> listZHWithdrawVO(Integer orderMonth, PlatformType source) {
    return customerWithdrawalMapper.listZHWithdraw(orderMonth, Optional.fromNullable(source)
        .transform(PlatformType.GET_CODE_FUNC).orNull());
  }

  /**
   * 查询非中行提现记录
   *
   * @param orderMonth 月份，格式为201809
   * @param source 平台, 若为空则为全平台
   * @return 查询结果
   */
  @Override
  public List<CommissionWithdrawVO> listNonZHWithdrawVO(Integer orderMonth, PlatformType source) {
    return customerWithdrawalMapper.listNonZHWithdraw(orderMonth, Optional.fromNullable(source)
        .transform(PlatformType.GET_CODE_FUNC).orNull());
  }

  /**
   * 查询提现记录
   *
   * @param cpId cpId
   * @param month 月份
   * @param source 平台
   * @return 提现记录list
   */
  @Override
  public List<CustomerWithdrawal> listWithDraw(Long cpId, Integer month, Integer source) {
    return customerWithdrawalMapper.listByCpIdAndMonth(cpId, month, source);
  }

  /**
   * 按月份更新状态
   */
  @Override
  public boolean updateCustomerWithdrawByMonth(CustomerWithdrawal withdrawal) {
    return customerWithdrawalMapper.updateByMonthSelective(withdrawal) > 0;
  }

  /**
   * 按id更新状态
   */
  @Override
  public boolean updateCustomerWithdrawById(CustomerWithdrawal withdrawal) {
    return customerWithdrawalMapper.updateByPrimaryKeySelective(withdrawal) > 0;
  }

  /**
   * 查询用户是否申请过提现
   *
   * @param cpId cpId
   * @param month 月份
   * @param source 平台
   * @return true or false
   */
  @Override
  public boolean isCpIdWithdrawed(Long cpId, Integer month, Integer source) {
    return customerWithdrawalMapper.selectIsWithdrawExists(cpId, month, source);
  }

  /**
   * 验证某月份是否已经处理过
   *
   * @param month 月份
   */
  @Override
  public boolean isOrderMonthProcessed(Integer month) {
    return customerWithdrawalMapper.selectOrderMonthProcessed(month);
  }

  @Override
  public List<String> listWithdrawTopDate(int limit, PlatformType platform) {
    return customerWithdrawalMapper.listTopDate(limit, platform.getCode());
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends BasePointCommAsst> List<T> listAsst(String bizId, Long cpId, Trancd trancd,
      Class<T> clazz) {
    if (clazz == PointSuspendingAsst.class) {
      return (List<T>) pointSuspendingAsstMapper.listAsst(bizId, cpId, trancd);
    } else if (clazz == CommissionSuspendingAsst.class) {
      return (List<T>) commissionSuspendingAsstMapper.listAsst(bizId, cpId, trancd);
    } else {
      throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "asst类型不支持");
    }
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

  /**
   * 插入德分audit表
   */
  private boolean insertPointAudit(PointTotal pointTotal,
      TotalAuditType totalAuditType, AuditType auditType) {
    PointTotalAudit audit = Transformer.fromBean(pointTotal, PointTotalAudit.class);
    audit.setId(pointTotal.getId());
    audit.setAuditType(auditType.getCode());
    audit.setAuditUser(totalAuditType.name());
    return pointTotalAuditMapper.insert(audit) > 0;
  }

  /**
   * 插入积分audit表
   */
  private boolean insertCommissionAudit(CommissionTotal commissionTotal,
      TotalAuditType totalAuditType, AuditType auditType) {
    CommissionTotalAudit audit = Transformer.fromBean(commissionTotal,
        CommissionTotalAudit.class);
    audit.setId(commissionTotal.getId());
    audit.setAuditType(auditType.getCode());
    audit.setAuditUser(totalAuditType.name());
    return commissionTotalAuditMapper.insert(audit) > 0;
  }

}
