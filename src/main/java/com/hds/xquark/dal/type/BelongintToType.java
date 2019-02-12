package com.hds.xquark.dal.type;

/** Created by wangxinhua. Date: 2018/8/29 Time: 下午3:55 */
public enum BelongintToType {
  NON(0, "Non", "默认平台"),

  /** 汉德森 */
  H(1, "Hds", "汉德森"),

  /** ViViLife */
  V(2, "ViviLife", "ViviLife"),

  /** 汉薇商城 */
  E(3, "Ecomm", "汉薇");

  private final int code;

  private final String fullName;

  private final String nameCN;

  BelongintToType(int code, String fullName, String nameCN) {
    this.code = code;
    this.fullName = fullName;
    this.nameCN = nameCN;
  }

  public int getCode() {
    return code;
  }

  public String getFullName() {
    return fullName;
  }

  public String getNameCN() {
    return nameCN;
  }
}
