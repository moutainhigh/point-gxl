package com.hds.xquark.service.point;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.hds.xquark.dal.constrant.PointConstrants;
import com.hds.xquark.dal.mapper.PointRecordMapper;
import com.hds.xquark.dal.mapper.PointTotalAuditMapper;
import com.hds.xquark.dal.mapper.PointTotalMapper;
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

/** @author wangxinhua.* @date 2018/12/12 */
@Service("PointServiceApi")
public class PointServiceApi implements TokenServiceApi<PointTotal, PointRecord> {

  private final PointTotalMapper pointTotalMapper;
  private final PointRecordMapper pointRecordMapper;
  private final PointTotalAuditMapper pointTotalAuditMapper;
  private final PointGradeService pointGradeService;

  @Autowired
  public PointServiceApi(
      PointTotalMapper pointTotalMapper,
      PointTotalAuditMapper pointTotalAuditMapper,
      PointGradeService pointGradeService,
      PointRecordMapper pointRecordMapper) {
    this.pointTotalMapper = pointTotalMapper;
    this.pointTotalAuditMapper = pointTotalAuditMapper;
    this.pointGradeService = pointGradeService;
    this.pointRecordMapper = pointRecordMapper;
  }

  @Override
  public Optional<PointTotal> loadTotal(Long cpId) {
    return Optional.fromNullable(pointTotalMapper.selectByCpId(cpId));
  }

  @Override
  public PointTotal initTotal(Long cpId) {
    boolean isExists = pointTotalMapper.selectTotalExists(cpId);
    if (isExists) {
      return loadTotal(cpId).get();
    }

    PointTotal total = BasePointCommTotal.emptyInfo(cpId, PointTotal.class);
    pointTotalMapper.insert(total);
    return total;
  }

  @Override
  public boolean updateTotal(PointTotal total) {
    saveAudit(total, AuditType.UPDATE);
    return pointTotalMapper.updateByPrimaryKeySelective(total) > 0;
  }

  @Override
  public boolean updateByCpId(PointTotal total) {
    return pointTotalMapper.updateByCpId(total) > 0;
  }

  @Override
  public boolean saveTotal(PointTotal total) {
    saveAudit(total, AuditType.INSERT);
    return pointTotalMapper.insert(total) > 0;
  }

  @Override
  public boolean saveAudit(PointTotal total, AuditType auditType) {
    PointTotalAudit audit = Transformer.fromBean(total, PointTotalAudit.class);
    audit.setId(total.getId());
    audit.setAuditType(auditType.getCode());
    audit.setAuditUser(TotalAuditType.API.name());
    return pointTotalAuditMapper.insert(audit) > 0;
  }

  @Override
  public PointCommOperationResult<PointTotal, PointRecord> modify(
      Long cpId,
      String bizId,
      Pair<FunctionCodeType, Trancd> bizPack,
      PlatformType platform,
      BigDecimal amount) {
    FunctionCodeType funcCodeType = bizPack.getLeft();
    GradeCode grade = pointGradeService.loadByFunctionCode(funcCodeType.getCode());
    Preconditions.checkNotNull(grade, "规则代码无效");
    Preconditions.checkArgument(
        grade.getCodeNumber().startsWith(String.valueOf(PointConstrants.POINT_CATEGORY)),
        "规则代码不匹配");
    BasePointCommOperator operator = PointOperatorFactory.getOperator(grade.getCodeName());
    PointCommOperationResult ret =
        operator.doOperation(
            cpId, bizId, grade, platform, amount, PointOperateType.POINT, bizPack.getRight());
    saveRet(bizId, grade, ret, operator, PointRecord.class);
    return ((PointCommOperationResult<PointTotal, PointRecord>) (ret));
  }

  @Override
  public PointCommOperationResult<PointTotal, PointRecord> consume(
      Long cpId, String bizId, Trancd tranCode, PlatformType platform, BigDecimal amount) {
    return modify(cpId, bizId, Pair.of(FunctionCodeType.CONSUME_POINT, tranCode), platform, amount);
  }

  @Override
  public PointCommOperationResult<PointTotal, PointRecord> grant(
      Long cpId, String bizId, Trancd tranCode, PlatformType platform, BigDecimal amount) {
    return modify(cpId, bizId, Pair.of(FunctionCodeType.GRANT_POINT, tranCode), platform, amount);
  }

  /**
   * 查询trancd相关的总数
   *
   * @param cpId cpId
   * @param trancd trancd
   * @return 查询record总额
   */
  public BigDecimal sumByTrancd(Long cpId, Trancd trancd) {
    return Optional.fromNullable(pointRecordMapper.sumByTrancd(cpId, trancd)).or(BigDecimal.ZERO);
  }

  @Override
  public Trancd transferCode() {
    return Trancd.TRANSFER_P;
  }
}
