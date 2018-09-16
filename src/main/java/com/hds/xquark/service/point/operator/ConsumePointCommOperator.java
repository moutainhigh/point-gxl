package com.hds.xquark.service.point.operator;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import com.hds.xquark.dal.mapper.CommissionSuspendingAsstMapper;
import com.hds.xquark.dal.mapper.PointSuspendingAsstMapper;
import com.hds.xquark.dal.model.BasePointCommAsst;
import com.hds.xquark.dal.model.BasePointCommRecord;
import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.model.CommissionRecord;
import com.hds.xquark.dal.model.CommissionSuspendingAsst;
import com.hds.xquark.dal.model.GradeCode;
import com.hds.xquark.dal.model.PointRecord;
import com.hds.xquark.dal.model.PointSuspendingAsst;
import com.hds.xquark.dal.type.CodeNameType;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.PointOperateType;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.service.error.BizException;
import com.hds.xquark.service.error.GlobalErrorCode;
import com.hds.xquark.service.point.PointCommCalResult;
import com.hds.xquark.service.point.PointCommOperationResult;
import com.hds.xquark.service.point.helper.PointCommCalHelper;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangxinhua on 2018/5/21. DESC:
 */
@Component
public class ConsumePointCommOperator extends BasePointCommOperator {

  private final PointSuspendingAsstMapper pointSuspendingAsstMapper;

  private final CommissionSuspendingAsstMapper commissionSuspendingAsstMapper;

  private final Map<Class<? extends BasePointCommRecord>, Class<? extends BasePointCommAsst>>
      ASST_MAPPINT =
      ImmutableMap.<Class<? extends BasePointCommRecord>, Class<? extends BasePointCommAsst>>of(
          PointRecord.class, PointSuspendingAsst.class,
          CommissionRecord.class, CommissionSuspendingAsst.class);

  @Autowired
  public ConsumePointCommOperator(
      PointSuspendingAsstMapper pointSuspendingAsstMapper,
      CommissionSuspendingAsstMapper commissionSuspendingAsstMapper) {
    this.pointSuspendingAsstMapper = pointSuspendingAsstMapper;
    this.commissionSuspendingAsstMapper = commissionSuspendingAsstMapper;
  }

  @Override
  public PointCommCalResult calRet(PointCommOperatorContext context) {
    BasePointCommTotal infoBefore = context.getTotal();
    BasePointCommTotal infoAfter = BasePointCommTotal.copy(infoBefore);
    // 消费积分
    BigDecimal consumePoint = context.getGradeCode().getPoint();
    Map<PlatformType, BigDecimal> detailMap = new HashMap<>();
    boolean consumeRet =
        PointCommCalHelper.minus(infoAfter, context.getPlatform(), consumePoint, detailMap);
    if (!consumeRet) {
      throw new BizException(GlobalErrorCode.UNKNOWN, "积分不足, 无法扣减");
    }
    return new PointCommCalResult(infoAfter, detailMap);
  }

  @Override
  public List<? extends BasePointCommRecord> saveBackRecord(
      String bizId,
      GradeCode grade,
      PointCommOperationResult calRet,
      Class<? extends BasePointCommRecord> clazz) {
    // 扣减需要保存多条积分记录
    Trancd trancd = calRet.getTrancd();
    List<? extends BasePointCommRecord> records = buildRecords(bizId, grade, calRet, clazz);
    Iterator<? extends BasePointCommRecord> iterator = records.iterator();

    Class<? extends BasePointCommAsst> asstClazz = ASST_MAPPINT.get(clazz);
    BasePointCommAsst asst =
        BasePointCommAsst.empty(asstClazz, calRet.getCpId(), grade, calRet.getPlatform(), trancd);
    while (iterator.hasNext()) {
      BasePointCommRecord record = iterator.next();
      if (record.getCurrent().signum() == 0) {
        iterator.remove();
        continue;
      }
      record.setRollbacked(false);
      saveRecord(record);
      // this should not work before feature/belonging-to being merged
      if (Objects.equals(record.getSource(), asst.getSource())) {
        asst.addRecord(record);
      }
    }
    saveAsst(asst);
    return records;
  }

  @Override
  public CodeNameType currType() {
    return CodeNameType.CONSUME;
  }

  @Override
  protected void preCheck(PointCommOperatorContext context, PointOperateType operateType) {
    super.preCheck(context, operateType);

    BigDecimal currPoint = context.getGradeCode().getPoint();
    if (currPoint == null || currPoint.signum() <= 0) {
      throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "扣减值无效");
    }

    Long cpId = context.getCpId();
    String bizId = context.getBusinessId();
    // 只针对订单类型才做校验
    Trancd trancd = context.getTrancd();
    if (Objects.equals(trancd, Trancd.DEDUCT_C) || Objects.equals(trancd, Trancd.DEDUCT_P)) {
      boolean hasRecord;
      if (operateType == PointOperateType.POINT) {
        hasRecord = pointRecordMapper.selectRecordExists(bizId, cpId, trancd);
      } else if (operateType == PointOperateType.COMMISSION) {
        hasRecord = commissionRecordMapper.selectRecordExists(bizId, cpId, trancd);
      } else {
        throw new BizException(GlobalErrorCode.POINT_NOT_SUPPORT);
      }
      if (hasRecord) {
        throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "该订单已扣减过, 请不要重复操作");
      }
    }
  }

  /**
   * 根据不同类型保存asst
   */
  private boolean saveAsst(BasePointCommAsst asst) {
    checkNotNull(asst);
    if (asst instanceof PointSuspendingAsst) {
      return pointSuspendingAsstMapper.insert((PointSuspendingAsst) asst) > 0;
    } else if (asst instanceof CommissionSuspendingAsst) {
      return commissionSuspendingAsstMapper.insert((CommissionSuspendingAsst) asst) > 0;
    } else {
      throw new IllegalStateException("asst type not allowed");
    }
  }
}
