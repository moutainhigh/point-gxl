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
  CPCM("推广费"),
  CBM("立省值"),
  EF("推荐奖"),
  CNRM("服务奖"),
  RELEASE("积分发放"),
  DEDUCT_P("德分扣减"),
  DEDUCT_C("积分扣减"),
  CANCEL_P("德分取消"),
  CANCEL_C("积分取消"),
  REVERT_P("德分退货"),
  REVERT_C("积分退货"),
  REWARD_P("德分奖励"),
  REWARD_C("积分奖励"),
  WITHDRAW_C("积分提现"),
  DEPOSIT_P("德分发放"),
  DEPOSIT_C("积分发放"),
  MIGRATE_P("德分迁移"),
  MIGRATE_C("积分迁移"),
  ROLLBACK_C("提现积分退回"),
  CNRM1("服务费1"),
  CNRM2("服务费2"),
  CBM1("立省值1"),
  CBM2("立省值2"),
  ACHA1("VIP首单推荐奖1"),
  ACHA2("VIP首单推荐奖2"),
  SOCIAL_P("社区德分"),
  PACKET_POINT("德分红包"),
  TRANSFER_C("德分转换"),
  TRANSFER_P("转为收益"),
  PACKET_RAIN("红包雨领取"),
  LOTTERY_EARN("抽奖领取"),
  FRESHMAN("新人升级发放德分"),
  FRESHMAN_P("新人发放德分扣减"),
  LOTTERY_F("新人首单抽奖发放德分"),
  DIS("商家折扣"),
  RB("立省"),
  PF("推广费"),
  DP1("首单反德分"),
  DP2("净值拨付"),
  DP3("奖励德分"),
  hvnc("新人本人20元"),
  hvns("上级2元积分"),
  hvnp("上级100德分"),
  FRESHMAN_C("新人发放积分扣减");
  private final String desc;

  Trancd(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }
}
