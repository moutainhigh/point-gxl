package com.xquark.service.point.operator;

import com.xquark.dal.model.BasePointCommRecord;
import com.xquark.dal.model.BasePointCommTotal;
import com.xquark.dal.model.GradeCode;
import com.xquark.dal.type.CodeNameType;
import com.xquark.dal.type.PlatformType;
import com.xquark.dal.type.PointRecordType;
import com.xquark.service.error.BizException;
import com.xquark.service.error.GlobalErrorCode;
import com.xquark.service.point.PointCommCalResult;
import com.xquark.service.point.PointCommOperationResult;
import com.xquark.service.point.helper.PointCommCalHelper;
import java.math.BigDecimal;
import java.util.HashMap;
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
  public boolean saveBackRecord(String bizId, GradeCode grade, PointCommOperationResult calRet,
      Class<? extends BasePointCommRecord> clazz) {
    // 扣减需要保存多条积分记录
    List<? extends BasePointCommRecord> records = buildRecords(bizId, grade, calRet,
        PointRecordType.DEDUCT,
        clazz);
    boolean ret = true;
    for (BasePointCommRecord record : records) {
      if (record.getCurrent().signum() == 0) {
        continue;
      }
      record.setRollbacked(false);
      ret = ret && saveRecord(record);
    }
    return ret;
  }

  @Override
  public CodeNameType currType() {
    return CodeNameType.CONSUME;
  }
}
