package com.hds.xquark.dal.vo;

import java.math.BigDecimal;

/**
 * Created by wangxinhua. Date: 2018/8/17 Time: 下午6:40
 */
public class CommissionWithdrawVO {

  private Long cpId;

  private String bankAccount;

  private String bankNumber;

  private String bankName;

  private String name;

  private BigDecimal amount;

  /**
   * 手机号
   */
  private String phone;

  /**
   * 证件号码
   */
  private String tinCode;

  /**
   * 备注
   */
  private String remark;

  /**
   * 提现错误信息
   */
  private String errorMsg;

  public Long getCpId() {
    return cpId;
  }

  public void setCpId(Long cpId) {
    this.cpId = cpId;
  }

  public String getBankAccount() {
    return bankAccount;
  }

  public void setBankAccount(String bankAccount) {
    this.bankAccount = bankAccount;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getTinCode() {
    return tinCode;
  }

  public void setTinCode(String tinCode) {
    this.tinCode = tinCode;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getBankNumber() {
    return bankNumber;
  }

  public void setBankNumber(String bankNumber) {
    this.bankNumber = bankNumber;
  }

  @Override
  public String toString() {
    return "CommissionWithdrawVO{" +
        "cpId=" + cpId +
        ", bankAccount='" + bankAccount + '\'' +
        ", bankNumber='" + bankNumber + '\'' +
        ", bankName='" + bankName + '\'' +
        ", name='" + name + '\'' +
        ", amount=" + amount +
        ", phone='" + phone + '\'' +
        ", tinCode='" + tinCode + '\'' +
        ", remark='" + remark + '\'' +
        ", errorMsg='" + errorMsg + '\'' +
        '}';
  }
}
