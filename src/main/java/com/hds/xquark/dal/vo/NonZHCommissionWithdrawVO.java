package com.hds.xquark.dal.vo;

/**
 * Created by wangxinhua. Date: 2018/8/17 Time: 下午6:50 非中行导出信息VO
 */
public class NonZHCommissionWithdrawVO extends BaseCommissionWithdrawVO {

  private String bankNumber;

  public String getBankNumber() {
    return bankNumber;
  }

  public void setBankNumber(String bankNumber) {
    this.bankNumber = bankNumber;
  }

}
