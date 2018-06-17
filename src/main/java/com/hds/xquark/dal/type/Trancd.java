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
  DEDUCT("积分扣减");

  private final String desc;

  Trancd(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }
}
