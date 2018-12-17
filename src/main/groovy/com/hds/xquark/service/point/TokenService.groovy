package com.hds.xquark.service.point

import com.hds.xquark.dal.model.BasePointCommRecord
import com.hds.xquark.dal.model.BasePointCommTotal
import com.hds.xquark.dal.model.GradeCode
import com.hds.xquark.dal.type.AuditType
import com.hds.xquark.dal.type.PlatformType
import com.hds.xquark.dal.type.TotalAuditType
import com.hds.xquark.dal.type.Trancd
import com.hds.xquark.service.error.BizException
import com.hds.xquark.service.error.GlobalErrorCode
import com.hds.xquark.service.point.operator.BasePointCommOperator
import com.hds.xquark.service.point.type.FunctionCodeType
import org.apache.commons.lang3.tuple.Pair
import org.springframework.transaction.annotation.Transactional

import java.lang.invoke.MethodHandleImpl

/**
 * @author wangxinhua.* @date 2018/12/12
 * 统一代币service, 代币可以指积分、德分等
 * 轻量级调用接口
 */
interface TokenService<T extends BasePointCommTotal, R extends BasePointCommRecord> extends TokenTrait<T, R> {

    /**
     * 查询总额
     * @param cpId 用户id
     * @return 总额
     */
    T loadTotal(Long cpId)

    /**
     * 更新总额
     * @param total 总额对象
     * @return 插入结果
     */
    boolean updateTotal(T total, TotalAuditType totalAuditType)

    boolean updateByCpId(T total, TotalAuditType totalAuditType)

    /**
     * 保存总额
     * @param total 总额对象
     * @param totalAuditType 审核类型
     * @return 保存结果
     */
    boolean saveTotal(T total, TotalAuditType totalAuditType)

    boolean saveAudit(T total, AuditType auditType, TotalAuditType totalAuditType)

    /**
     * 操作总额并追加相应的记录
     * @param cpId 用户id
     * @param bizId 业务id
     * @param bizPack 业务包装
     * @param platform 操作平台
     * @param amount 操作数量
     * @return 操作结果
     */
    PointCommOperationResult<T, R> modify(Long cpId, String bizId,
                                          Pair<FunctionCodeType, Trancd> bizPack, PlatformType platform,
                                          BigDecimal amount)

}

trait TokenTrait<T extends BasePointCommTotal, R extends BasePointCommRecord> {

    /**
     * 更新总额
     * @param total 总额对象
     * @return 插入结果
     */
    abstract boolean updateTotal(T total, TotalAuditType totalAuditType)

    /**
     * 保存总额
     * @param total 总额对象
     * @param totalAuditType 审核类型
     * @return 保存结果
     */
    abstract boolean saveTotal(T total, TotalAuditType totalAuditType)

    /**
     * 保存结果
     * @param bizId 业务id
     * @param grade 规则对象
     * @param operationResult 计算结果
     * @param operator 计算类
     * @param clazz 保存类型
     * @param auditType 审核类型
     */
    @Transactional(rollbackFor = Exception.class)
    void saveRet(String bizId, GradeCode grade, PointCommOperationResult<T, R> operationResult,
                 BasePointCommOperator operator,
                 Class<? extends BasePointCommRecord> clazz, TotalAuditType auditType) {
        // 保存记录
        List<? extends BasePointCommRecord> records = operator
                .saveBackRecord(bizId, grade, operationResult, clazz)

        // 更新或保存用户积分信息
        T infoAfter = operationResult.getInfoAfter()
        boolean ret = infoAfter.getId() == null ? saveTotal(infoAfter, auditType)
                : updateTotal(infoAfter, auditType)
        if (!ret) {
            throw new BizException(GlobalErrorCode.INTERNAL_ERROR, "内部错误, 请稍后再试");
        }
        operationResult.setCurrRecords(records)
    }

}
