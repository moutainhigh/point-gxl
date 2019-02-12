package com.hds.xquark.service.point;

import com.google.common.base.Optional;
import com.hds.xquark.dal.constrant.PointConstrants;
import com.hds.xquark.dal.mapper.CommissionTotalAuditMapper;
import com.hds.xquark.dal.mapper.CommissionTotalMapper;
import com.hds.xquark.dal.model.*;
import com.hds.xquark.dal.type.*;
import com.hds.xquark.dal.util.Transformer;
import com.hds.xquark.service.point.operator.BasePointCommOperator;
import com.hds.xquark.service.point.operator.PointOperatorFactory;
import com.hds.xquark.service.point.type.FunctionCodeType;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author wangxinhua.
 * @date 2018/12/12 积分操作类
 */
@Service("CommissionService")
public class CommissionServiceApi implements TokenServiceApi<CommissionTotal, CommissionRecord> {

  private final CommissionTotalMapper commissionTotalMapper;
  private final CommissionTotalAuditMapper commissionTotalAuditMapper;
  private final PointGradeService pointGradeService;

  @Autowired
  CommissionServiceApi(
      CommissionTotalMapper commissionTotalMapper,
      CommissionTotalAuditMapper commissionTotalAuditMapper,
      PointGradeService pointGradeService) {
    this.commissionTotalMapper = commissionTotalMapper;
    this.commissionTotalAuditMapper = commissionTotalAuditMapper;
    this.pointGradeService = pointGradeService;
  }

  @Override
  public Optional<CommissionTotal> loadTotal(Long cpId) {
    return Optional.fromNullable(commissionTotalMapper.selectByCpId(cpId));
  }

  @Override
  public CommissionTotal initTotal(Long cpId) {
    boolean isExists = commissionTotalMapper.selectTotalExists(cpId);
    if (isExists) {
      return loadTotal(cpId).get();
    }
    CommissionTotal total = BasePointCommTotal.emptyInfo(cpId, CommissionTotal.class);
    commissionTotalMapper.insert(total);
    return total;
  }

  @Override
  public boolean updateTotal(CommissionTotal total) {
    saveAudit(total, AuditType.UPDATE);
    return commissionTotalMapper.updateByPrimaryKeySelective(total) > 0;
  }

  @Override
  public boolean updateByCpId(CommissionTotal total) {
    return commissionTotalMapper.updateByCpId(total) > 0;
  }

  @Override
  public boolean saveTotal(CommissionTotal total) {
    saveAudit(total, AuditType.INSERT);
    return commissionTotalMapper.insert(total) > 0;
  }

  @Override
  public boolean saveAudit(CommissionTotal total, AuditType auditType) {
    CommissionTotalAudit audit = Transformer.fromBean(total, CommissionTotalAudit.class);
    audit.setId(total.getId());
    audit.setAuditType(auditType.getCode());
    audit.setAuditUser(TotalAuditType.API.name());
    return commissionTotalAuditMapper.insert(audit) > 0;
  }

  @Override
  public PointCommOperationResult<CommissionTotal, CommissionRecord> modify(
      Long cpId,
      String bizId,
      Pair<FunctionCodeType, Trancd> bizPack,
      PlatformType platform,
      BigDecimal amount) {
    FunctionCodeType funcCodeType = bizPack.getLeft();
    GradeCode grade = pointGradeService.loadByFunctionCode(funcCodeType.getCode());
    checkNotNull(grade, "规则代码无效");
    checkArgument(
        grade.getCodeNumber().startsWith(String.valueOf(PointConstrants.COMMISSION_CATEGORY)),
        "规则代码不匹配");
    BasePointCommOperator operator = PointOperatorFactory.getOperator(grade.getCodeName());
    PointCommOperationResult ret =
        operator.doOperation(
            cpId, bizId, grade, platform, amount, PointOperateType.COMMISSION, bizPack.getRight());
    saveRet(bizId, grade, ret, operator, CommissionRecord.class);
    return ret;
  }

  @Override
  public PointCommOperationResult<CommissionTotal, CommissionRecord> consume(
      Long cpId, String bizId, Trancd tranCode, PlatformType platform, BigDecimal amount) {
    return modify(
        cpId, bizId, Pair.of(FunctionCodeType.CONSUME_COMMISSION, tranCode), platform, amount);
  }

  @Override
  public PointCommOperationResult<CommissionTotal, CommissionRecord> grant(
      Long cpId, String bizId, Trancd tranCode, PlatformType platform, BigDecimal amount) {
    return modify(
        cpId, bizId, Pair.of(FunctionCodeType.GRANT_COMMISSION, tranCode), platform, amount);
  }

  @Override
  public Trancd transferCode() {
    return Trancd.TRANSFER_C;
  }
}
