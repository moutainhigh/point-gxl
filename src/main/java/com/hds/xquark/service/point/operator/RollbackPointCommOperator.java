package com.hds.xquark.service.point.operator;

import com.google.common.base.Preconditions;
import com.hds.xquark.dal.constrant.GradeCodeConstrants;
import com.hds.xquark.dal.mapper.CommissionRecordMapper;
import com.hds.xquark.dal.mapper.PointRecordMapper;
import com.hds.xquark.dal.model.BasePointCommRecord;
import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.model.CommissionRecord;
import com.hds.xquark.dal.model.GradeCode;
import com.hds.xquark.dal.model.PointRecord;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangxinhua on 2018/5/21. DESC:
 */
@Component
public class RollbackPointCommOperator extends BasePointCommOperator {

  private final PointRecordMapper pointRecordMapper;

  private final CommissionRecordMapper commissionRecordMapper;

  @Autowired
  public RollbackPointCommOperator(PointRecordMapper pointRecordMapper,
      CommissionRecordMapper commissionRecordMapper) {
    this.pointRecordMapper = pointRecordMapper;
    this.commissionRecordMapper = commissionRecordMapper;
  }

  @Override
  @SuppressWarnings("unchecked")
  public PointCommCalResult calRet(PointCommOperatorContext context) {
    Trancd trancd = context.getTrancd();
    Preconditions.checkNotNull(trancd, "积分记录类型不能为空");

    String businessId = context.getBusinessId();
    Long cpId = context.getCpId();
    BasePointCommTotal infoBefore = context.getTotal();
    BasePointCommTotal infoAfter = BasePointCommTotal.copy(infoBefore);

    // 同一业务id只会回退最后一次操作
    List<? extends BasePointCommRecord> records = findUnRollBackedList(businessId, cpId, trancd,
        context.getOperateType().getRecordClazz());
    if (CollectionUtils.isEmpty(records)) {
      throw new BizException(GlobalErrorCode.POINT_RECORD_NOT_FOUNT);
    }
    Map<PlatformType, BigDecimal> detailMap = new HashMap<>(records.size());
    for (BasePointCommRecord record : records) {
      if (isRecordRollbacked(record)) {
        throw new BizException(GlobalErrorCode.POINT_BACKED);
      }
      PlatformType platform;
      if (StringUtils.equals(record.getCodeNumber(), GradeCodeConstrants.CONSUME_COMMISSION_CODE)
          || StringUtils.equals(record.getCodeNumber(), GradeCodeConstrants.CONSUME_POINT_CODE)) {
        platform = PlatformType.fromCode(record.getBelongingTo());
      } else {
        platform = record.getPlatForm();
      }
      // 修改记录
      BigDecimal negateUsable = record.getCurrent().negate();
      BigDecimal negateFreeze = record.getCurrentFreezed().negate();
      PointCommCalHelper.plus(infoAfter, platform, negateUsable);
      PointCommCalHelper
          .plusFreeze(infoAfter, platform, negateFreeze);
      // TODO map中的value没有使用意义
      detailMap.put(platform, record.getCurrent());
    }
    return new PointCommCalResult(infoAfter, (List<BasePointCommRecord>) records, detailMap);
  }

  @Override
  public CodeNameType currType() {
    return CodeNameType.ROLLBACK;
  }

  @Override
  protected void preCheck(PointCommOperatorContext context,
      PointOperateType operateType) {
    // do nothing
  }

  /**
   * 覆盖回滚记录保存方式，保存新的回滚记录同时更新原记录的字段
   *
   * @param bizId 业务id
   * @param grade 积分规则
   * @param calRet 计算结果
   * @return 保存结果
   */
  @Override
  public List<? extends BasePointCommRecord> saveBackRecord(String bizId, GradeCode grade,
      PointCommOperationResult calRet,
      Class<? extends BasePointCommRecord> clazz) {
    // 找出回滚了多少积分
    List<? extends BasePointCommRecord> rollBackRecords = buildRecords(bizId, grade,
        calRet, calRet.getTrancd(), clazz);
    for (BasePointCommRecord record : rollBackRecords) {
      record.setRollbacked(false);
      saveRecord(record);
    }

    // 更新原记录为已回滚
    List<BasePointCommRecord> rollBacked = calRet.getRollBacked();
    if (CollectionUtils.isEmpty(rollBacked)) {
      throw new BizException(GlobalErrorCode.INTERNAL_ERROR, "回滚记录不存在");
    }
    // 找到对应平台的回滚记录并设置为已回滚
    // TODO 回滚记录数据量不大, 嵌套循环暂时不会造成性能损耗
    for (BasePointCommRecord record : rollBacked) {
      for (BasePointCommRecord backedRec : rollBackRecords) {
        boolean condition;
        if (StringUtils.equals(record.getCodeNumber(), GradeCodeConstrants.CONSUME_POINT_CODE)
            || StringUtils
            .equals(record.getCodeNumber(), GradeCodeConstrants.CONSUME_COMMISSION_CODE)) {
          condition = Objects.equals(backedRec.getSource(), record.getBelongingTo());
        } else {
          condition = Objects.equals(backedRec.getSource(), record.getSource());
        }
        if (condition) {
          record.setRollbackId(backedRec.getId());
          record.setRollbacked(true);
          updateRecord(record);
        }
      }
    }
    return rollBackRecords;
  }

  /**
   * 判断记录是否是已回滚记录
   */
  private boolean isRecordRollbacked(BasePointCommRecord record) {
    String codeNumber = record.getCodeNumber();
    return codeNumber.equals(GradeCodeConstrants.CANCEL_COMMISSION_CODE)
        || codeNumber.equals(GradeCodeConstrants.CANCEL_POINT_CODE)
        || codeNumber.equals(GradeCodeConstrants.RETURN_COMMISSION_CODE)
        || codeNumber.equals(GradeCodeConstrants.RETURN_POINT_CODE)
        || codeNumber.equals(GradeCodeConstrants.RELEASE_COMMISSION_CODE)
        || codeNumber.equals(GradeCodeConstrants.RELEASE_POINT_CODE);
  }

  @SuppressWarnings("unchecked")
  private <T extends BasePointCommRecord> List<T> findUnRollBackedList(String bizId,
      Long cpId, Trancd recordType,
      Class<T> clazz) {
    if (clazz == PointRecord.class) {
      return (List<T>) pointRecordMapper
          .listUnRollBackedByCpIdWithBizIdAndType(bizId, cpId, recordType);
    } else if (clazz == CommissionRecord.class) {
      return (List<T>) commissionRecordMapper
          .listUnRollBackedByCpIdWithBizIdAndType(bizId, cpId, recordType);
    } else {
      throw new BizException(GlobalErrorCode.POINT_NOT_SUPPORT);
    }
  }
}
