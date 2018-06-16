package com.xquark.dal.type;

/**
 * created by
 *
 * @author wangxinhua at 18-6-15 上午10:26
 */
public enum PointRecordType {

  PRBA("增加积分"), CNRA("增加积分2"),
  DEDUCT("扣减积分"), ROLLBACK("回滚积分"),
  FREEZE("冻结积分");

  private final String desc;

  PointRecordType(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }
}
