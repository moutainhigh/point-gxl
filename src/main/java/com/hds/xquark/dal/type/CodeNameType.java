package com.hds.xquark.dal.type;


import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * @author wangxinhua on 2018/5/18. DESC:
 */
public enum CodeNameType {

  /**
   * 发放积分
   */
  GRANT(1, LongOperation.PLUS),

  /**
   * 消费积分
   */
  CONSUME(2, LongOperation.MINUS),

  /**
   * 回退积分
   */
  ROLLBACK(3, null),

  FREEZE(4, null),

  /**
   * 添加积分并立即冻结
   */
  FREEZE_GRANT(5, null),

  /**
   * 解冻积分
   */
  UNFREEZE(6, LongOperation.PLUS);

  private final int code;

  private final LongOperation operation;

  private static final CodeNameType[] TYPE_ARR = CodeNameType.values();

  CodeNameType(int code, LongOperation operation) {
    this.code = code;
    this.operation = operation;
  }

  public int getCode() {
    return code;
  }

  /**
   * 获取数字操作类型
   */
  public Optional<LongOperation> getOperation() {
    return Optional.fromNullable(operation);
  }

  public static CodeNameType fromCode(int code) {
    code -= 1;
    Preconditions.checkArgument(code >= 0 && code < TYPE_ARR.length,
        "规则代码不合法");
    return TYPE_ARR[code];
  }

}
