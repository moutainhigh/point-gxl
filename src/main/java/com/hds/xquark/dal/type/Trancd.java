package com.hds.xquark.dal.type;

/**
 * created by
 *
 * @author wangxinhua at 18-6-15 上午10:26
 */
public enum Trancd {

  DF("德分"),
  CPCA("零售提成"),
  CNRA("零售提成(级差)"),
  NQBA("晋级奖"),
  ROYA("市场管理奖"),
  PRBA("行政管理奖"),
  CNBA("年度分红"),
  CAR("车奖"),
  VF1("加入礼"),
  ACHA("推荐奖1"),
  VF2("推荐奖2"),
  CPCB("市场发展奖"),
  CNRB("市场服务奖"),
  CBA("购货权益1"),
  VF3("购货权益"),
  CPCM("推荐奖"),
  CBM("购货权益"),
  EF("推荐奖2"),
  RELEASE("积分发放"),
  DEDUCT_P("德分扣减"),
  DEDUCT_C("积分扣减"),
  CANCEL_P("德分取消"),
  CANCEL_C("积分取消"),
  REVERT_P("积分退货"),
  REVERT_C("德分退货"),
  REWARD_P("德分奖励"),
  REWARD_C("积分奖励"),
  WITHDRAW_C("积分提现"),
  DEPOSIT_P("德分发放"),
  DEPOSIT_C("积分发放"),
  MIGRATE_P("德分迁移"),
  MIGRATE_C("积分迁移");

  private final String desc;

  Trancd(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }
}
