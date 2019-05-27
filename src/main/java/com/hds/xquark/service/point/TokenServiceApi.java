package com.hds.xquark.service.point;

import com.google.common.base.Optional;
import com.hds.xquark.dal.model.BasePointCommRecord;
import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.model.GradeCode;
import com.hds.xquark.dal.type.AuditType;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.service.error.BizException;
import com.hds.xquark.service.error.GlobalErrorCode;
import com.hds.xquark.service.point.operator.BasePointCommOperator;
import com.hds.xquark.service.point.type.FunctionCodeType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/** @author wangxinhua.* @date 2018/12/12 统一代币service, 代币可以指积分、德分等 轻量级调用接口 */
interface TokenServiceApi<T extends BasePointCommTotal, R extends BasePointCommRecord> {

  Logger LOGGER = Logger.getLogger(TokenServiceApi.class);

  /**
   * 查询总额
   *
   * @param cpId 用户id
   * @return 总额
   */
  Optional<T> loadTotal(Long cpId);

  /**
   * 初始化积分数据
   *
   * @param cpId 用户id
   * @return
   */
  T initTotal(Long cpId);

  /**
   * 更新总额
   *
   * @param total 总额对象
   * @return 插入结果
   */
  boolean updateTotal(T total);

  boolean updateByCpId(T total);

  /**
   * 保存总额
   *
   * @param total 总额对象
   * @return 保存结果
   */
  boolean saveTotal(T total);

  boolean saveAudit(T total, AuditType auditType);

  /**
   * 操作总额并追加相应的记录
   *
   * @param cpId 用户id
   * @param bizId 业务id
   * @param bizPack 业务包装
   * @param platform 操作平台
   * @param amount 操作数量
   * @return 操作结果
   */
  PointCommOperationResult<T, R> modify(
      Long cpId,
      String bizId,
      Pair<FunctionCodeType, Trancd> bizPack,
      PlatformType platform,
      BigDecimal amount);
  /**
   * 消费总额并追加记录
   *
   * @param cpId 用户id
   * @param bizId 业务id
   * @param tranCode 业务code
   * @param platform 操作平台
   * @param amount 操作数量
   * @return 操作结果
   */
  PointCommOperationResult<T, R> consume(
      Long cpId, String bizId, Trancd tranCode, PlatformType platform, BigDecimal amount);

  /**
   * 增加总额并追加记录
   *
   * @param cpId 用户id
   * @param bizId 业务id
   * @param tranCode 业务code
   * @param platform 操作平台
   * @param amount 操作数量
   * @return 操作结果
   */
  PointCommOperationResult<T, R> grant(
      Long cpId, String bizId, Trancd tranCode, PlatformType platform, BigDecimal amount);

  /**
   * 从自身消费并转换到 target service中
   *
   * @param cpId cpId
   * @param amount 数量
   * @param platform 平台类型
   * @param rate 转换倍率
   * @param target 目标service
   */
  @Transactional(rollbackFor = Exception.class)
  default void transform(
      Long cpId,
      BigDecimal amount,
      PlatformType platform,
      BigDecimal rate,
      TokenServiceApi target) {
    Trancd thisCode = transferCode();
    Trancd targetCode = target.transferCode();
    String thisBizId =
        StringUtils.substringBefore(getClass().getSimpleName(), "Service").toLowerCase()
            + " transfer";
    String targetBizId =
        StringUtils.substringBefore(target.getClass().getSimpleName(), "Service").toLowerCase()
            + " transfer";

    consume(cpId, thisBizId, thisCode, platform, amount);
    target.grant(
        cpId, targetBizId, targetCode, platform, amount.divide(rate, 4, RoundingMode.HALF_EVEN));
    LOGGER.info(
        String.format(
            "cpId: %s :: transfer %s => %s with amount %s", cpId, thisCode, targetCode, amount));
  }

  /**
   * 保存结果
   *
   * @param bizId 业务id
   * @param grade 规则对象
   * @param operationResult 计算结果
   * @param operator 计算类
   * @param clazz 保存类型
   */
  @Transactional(rollbackFor = Exception.class)
  default void saveRet(
      String bizId,
      GradeCode grade,
      PointCommOperationResult<T, R> operationResult,
      BasePointCommOperator operator,
      Class<? extends BasePointCommRecord> clazz) {
    // 保存记录
    List<? extends BasePointCommRecord> records =
        operator.saveBackRecord(bizId, grade, operationResult, clazz);

    // 更新或保存用户积分信息
    T infoAfter = operationResult.getInfoAfter();
    boolean ret = infoAfter.getId() == null ? saveTotal(infoAfter) : updateTotal(infoAfter);
    if (!ret) {
      throw new BizException(GlobalErrorCode.INTERNAL_ERROR, "内部错误, 请稍后再试");
    }
    operationResult.setCurrRecords((List<R>) records);
  }

  /**
   * 获取当前service的转换code
   *
   * @return Trancd
   */
  Trancd transferCode();
}
