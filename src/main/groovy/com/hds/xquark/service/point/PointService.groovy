package com.hds.xquark.service.point

import com.hds.xquark.dal.constrant.PointConstrants
import com.hds.xquark.dal.mapper.PointTotalAuditMapper
import com.hds.xquark.dal.mapper.PointTotalMapper
import com.hds.xquark.dal.model.PointRecord
import com.hds.xquark.dal.model.PointTotal
import com.hds.xquark.dal.model.PointTotalAudit
import com.hds.xquark.dal.type.*
import com.hds.xquark.dal.util.Transformer
import com.hds.xquark.service.point.operator.PointOperatorFactory
import com.hds.xquark.service.point.type.FunctionCodeType
import org.apache.commons.lang3.tuple.Pair
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkNotNull

/**
 * @author wangxinhua.* @date 2018/12/12
 */
@Service("PointService")
class PointService implements TokenService<PointTotal, PointRecord> {

    private final PointTotalMapper pointTotalMapper

    private final PointTotalAuditMapper pointTotalAuditMapper

    private final PointGradeService pointGradeService

    @Autowired
    PointService(PointTotalMapper pointTotalMapper, PointTotalAuditMapper pointTotalAuditMapper, PointGradeService pointGradeService) {
        this.pointTotalMapper = pointTotalMapper
        this.pointTotalAuditMapper = pointTotalAuditMapper
        this.pointGradeService = pointGradeService
    }

    @Override
    PointTotal loadTotal(Long cpId) {
        pointTotalMapper.selectByCpId(cpId)
    }

    @Override
    boolean updateTotal(PointTotal total) {
        saveAudit(total, AuditType.UPDATE)
        pointTotalMapper.updateByPrimaryKeySelective(total)
    }

    @Override
    boolean updateByCpId(PointTotal total) {
        pointTotalMapper.updateByCpId(total)
    }

    @Override
    boolean saveTotal(PointTotal total) {
        saveAudit(total, AuditType.INSERT)
        pointTotalMapper.insert(total)
    }

    @Override
    boolean saveAudit(PointTotal total, AuditType auditType) {
        PointTotalAudit audit = Transformer.fromBean(total, PointTotalAudit.class)
        audit.setId(total.getId())
        audit.setAuditType(auditType.getCode())
        audit.setAuditUser(TotalAuditType.API.name())
        pointTotalAuditMapper.insert(audit) > 0
    }

    @Override
    PointCommOperationResult<PointTotal, PointRecord> modify(Long cpId, String bizId, Pair<FunctionCodeType, Trancd> bizPack, PlatformType platform, BigDecimal amount) {
        def funcCodeType = bizPack.getLeft()
        def grade = pointGradeService.loadByFunctionCode(funcCodeType.getCode())
        checkNotNull(grade, "规则代码无效")
        checkArgument(grade.codeNumber.startsWith(PointConstrants.POINT_CATEGORY as String), "规则代码不匹配")
        def operator = PointOperatorFactory.getOperator(grade.getCodeName())
        def ret = operator.doOperation(cpId, bizId, grade, platform, amount, PointOperateType.POINT, bizPack.getRight())
        saveRet(bizId, grade, ret, operator, PointRecord.class, TotalAuditType.API)
        ret
    }
}
