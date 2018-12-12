package com.hds.xquark.service.point

import com.hds.xquark.dal.mapper.CommissionTotalAuditMapper
import com.hds.xquark.dal.mapper.CommissionTotalMapper
import com.hds.xquark.dal.model.CommissionRecord
import com.hds.xquark.dal.model.CommissionTotal
import com.hds.xquark.dal.model.CommissionTotalAudit
import com.hds.xquark.dal.model.PointTotalAudit
import com.hds.xquark.dal.type.AuditType
import com.hds.xquark.dal.type.PlatformType
import com.hds.xquark.dal.type.PointOperateType
import com.hds.xquark.dal.type.TotalAuditType
import com.hds.xquark.dal.type.Trancd
import com.hds.xquark.dal.util.Transformer
import com.hds.xquark.service.point.operator.PointOperatorFactory
import com.hds.xquark.service.point.type.FunctionCodeType
import org.apache.commons.lang3.tuple.Pair
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author wangxinhua.
 * @date 2018/12/12
 * 积分操作类
 */
@Service("CommissionService")
class CommissionService implements TokenService<CommissionTotal, CommissionRecord> {

    private final CommissionTotalMapper commissionTotalMapper
    private final CommissionTotalAuditMapper commissionTotalAuditMapper
    private final PointGradeService pointGradeService

    @Autowired
    CommissionService(CommissionTotalMapper commissionTotalMapper, CommissionTotalAuditMapper commissionTotalAuditMapper, PointGradeService pointGradeService) {
        this.commissionTotalMapper = commissionTotalMapper
        this.commissionTotalAuditMapper = commissionTotalAuditMapper
        this.pointGradeService = pointGradeService
    }

    @Override
    CommissionTotal loadTotal(Long cpId) {
        commissionTotalMapper.selectByCpId(cpId)
    }

    @Override
    boolean updateTotal(CommissionTotal total, TotalAuditType auditType) {
        saveAudit(total, AuditType.UPDATE, auditType)
        commissionTotalMapper.updateByPrimaryKeySelective(total)
    }

    @Override
    boolean saveTotal(CommissionTotal total, TotalAuditType totalAuditType) {
        saveAudit(total, AuditType.INSERT, totalAuditType)
        commissionTotalMapper.insert(total)
    }

    @Override
    boolean saveAudit(CommissionTotal total, AuditType auditType, TotalAuditType totalAuditType) {
        CommissionTotalAudit audit = Transformer.fromBean(total, CommissionTotalAudit.class)
        audit.setId(total.getId())
        audit.setAuditType(auditType.getCode())
        audit.setAuditUser(totalAuditType.name())
        commissionTotalAuditMapper.insert(audit) > 0
    }

    @Override
    PointCommOperationResult<CommissionTotal, CommissionRecord> modify(Long cpId, String bizId, Pair<FunctionCodeType, Trancd> bizPack, PlatformType platform, BigDecimal amount) {
        def funcCodeType = bizPack.getLeft()
        def grade = pointGradeService.loadByFunctionCode(funcCodeType.getCode())
        checkNotNull(grade, "规则代码无效")
        def operator = PointOperatorFactory.getOperator(grade.getCodeName())
        def ret = operator.doOperation(cpId, bizId, grade, platform, amount, PointOperateType.COMMISSION, bizPack.getRight())
//        saveRet(bizId, grade, ret, operator, CommissionRecord.class, TotalAuditType.API)
        ret
    }
}
