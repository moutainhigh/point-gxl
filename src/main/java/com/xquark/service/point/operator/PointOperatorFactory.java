package com.xquark.service.point.operator;

import static com.xquark.dal.type.CodeNameType.CONSUME;
import static com.xquark.dal.type.CodeNameType.FREEZE_GRANT;
import static com.xquark.dal.type.CodeNameType.GRANT;
import static com.xquark.dal.type.CodeNameType.ROLLBACK;

import com.google.common.collect.ImmutableMap;
import com.xquark.dal.type.CodeNameType;
import com.xquark.dal.util.SpringContextUtil;
import com.xquark.service.error.BizException;
import com.xquark.service.error.GlobalErrorCode;
import java.util.Map;

/**
 * @author wangxinhua on 2018/5/21. DESC:
 */
public class PointOperatorFactory {

  /**
   * 积分类型与操作映射关系
   */
  private static final Map<CodeNameType, Class<? extends BasePointCommOperator>> TYPE_OPERATOR_MAP;

  static {
    TYPE_OPERATOR_MAP = ImmutableMap.of(
        GRANT, GrantPointCommOperator.class,
        CONSUME, ConsumePointCommOperator.class,
        ROLLBACK, RollbackPointCommOperator.class,
        FREEZE_GRANT, FreezeGrantPointCommOperator.class
    );
  }

  /**
   * 根据type获取operator
   * @param type 积分规则类型
   * @return instance of {@link BasePointCommOperator}
   */
  public static BasePointCommOperator getOperator(CodeNameType type) {
    return getOperator(type, BasePointCommOperator.class);
  }

  /**
   * 根据type获取operator
   * @param type 积分规则类型
   * @param clazz 对应的operator的clazz, 用于类型推到
   * @param <T> 返回的operator类型
   * @return instance of {@link BasePointCommOperator}
   * @throws ClassCastException 如果指定的clazz类型与map中的不匹配
   */
  @SuppressWarnings("unchecked")
  public static <T extends BasePointCommOperator> T getOperator(CodeNameType type, Class<T> clazz) {
    Class<? extends BasePointCommOperator> opClazz = TYPE_OPERATOR_MAP.get(type);
    if (opClazz == null) {
      throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,
          String.format("类型 %s 未配置对应的处理类", type));
    }
    return (T) SpringContextUtil.getBean(opClazz);
  }

}
