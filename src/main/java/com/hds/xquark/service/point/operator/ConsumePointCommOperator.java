package com.hds.xquark.service.point.operator;

import com.hds.xquark.dal.model.BasePointCommRecord;
import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.model.GradeCode;
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
import org.springframework.stereotype.Component;

/**
 * @author wangxinhua on 2018/5/21. DESC:
 */
@Component
public class ConsumePointCommOperator extends BasePointCommOperator {

  @Override
  public PointCommCalResult calRet(PointCommOperatorContext context) {
    BasePointCommTotal infoBefore = context.getTotal();
    BasePointCommTotal infoAfter = BasePointCommTotal.copy(infoBefore);
    // 消费积分
    BigDecimal consumePoint = context.getGradeCode().getPoint();
    Map<PlatformType, BigDecimal> detailMap = new HashMap<>();
    boolean consumeRet = PointCommCalHelper.minus(infoAfter, context.getPlatform(), consumePoint,
        detailMap);
    if (!consumeRet) {
      throw new BizException(GlobalErrorCode.UNKNOWN, "积分不足, 无法扣减");
    }
    return new PointCommCalResult(infoAfter, detailMap);
  }

  @Override
  public List<? extends BasePointCommRecord> saveBackRecord(String bizId, GradeCode grade,
      PointCommOperationResult calRet,
      Class<? extends BasePointCommRecord> clazz) {
    // 扣减需要保存多条积分记录
    List<? extends BasePointCommRecord> records = buildRecords(bizId, grade, calRet,
        calRet.getTrancd(), clazz);
    Iterator<? extends BasePointCommRecord> iterator = records.iterator();
    while (iterator.hasNext()) {
      BasePointCommRecord record = iterator.next();
      if (record.getCurrent().signum() == 0) {
        iterator.remove();
      }
      record.setRollbacked(false);
      saveRecord(record);
    }
    return records;
  }

  @Override
  public CodeNameType currType() {
    return CodeNameType.CONSUME;
  }

  @Override
  protected void preCheck(PointCommOperatorContext context,
      PointOperateType operateType) {
    super.preCheck(context, operateType);

    BigDecimal currPoint = context.getGradeCode().getPoint();
    if (currPoint == null || currPoint.signum() <= 0) {
      throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "扣减值无效");
    }

    Long cpId = context.getCpId();
    String bizId = context.getBusinessId();
    Trancd trancd = context.getTrancd();
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
