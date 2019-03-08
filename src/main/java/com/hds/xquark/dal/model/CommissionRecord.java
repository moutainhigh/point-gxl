package com.hds.xquark.dal.model;

import com.hds.xquark.dal.type.Trancd;

import java.math.BigDecimal;

/**
 * commissionsuspending
 *
 * @author wangxinhua
 */
public class CommissionRecord extends BasePointCommRecord {

  /** 本次佣金 */
  private BigDecimal currentComm;

  /** 本次冻结佣金 */
  private BigDecimal currentFreezedComm;

  /** 本次不可提现佣金 */
  private BigDecimal currentNoWithdrawalComm;

  private Trancd trancd;

  public BigDecimal getCurrentComm() {
    return currentComm;
  }

  public void setCurrentComm(BigDecimal currentComm) {
    this.currentComm = currentComm;
  }

  public BigDecimal getCurrentFreezedComm() {
    return currentFreezedComm;
  }

  public void setCurrentFreezedComm(BigDecimal currentFreezedComm) {
    this.currentFreezedComm = currentFreezedComm;
  }

  public BigDecimal getCurrentNoWithdrawalComm() {
    return currentNoWithdrawalComm;
  }

  public void setCurrentNoWithdrawalComm(BigDecimal currentNoWithdrawalComm) {
    this.currentNoWithdrawalComm = currentNoWithdrawalComm;
  }

  public Trancd getTrancd() {
    return trancd;
  }

  public void setTrancd(Trancd trancd) {
    this.trancd = trancd;
  }

  @Override
  public BigDecimal getCurrent() {
    return getCurrentComm();
  }

  @Override
  public void setCurrent(BigDecimal current) {
    setCurrentComm(current);
  }

  @Override
  public BigDecimal getCurrentFreezed() {
    return getCurrentFreezedComm();
  }

  @Override
  public void setCurrentFreezed(BigDecimal currentFreezed) {
    setCurrentFreezedComm(currentFreezed);
  }

  @Override
  public BigDecimal getCurrentNoWithdrawal() {
    return getCurrentNoWithdrawalComm();
  }

  @Override
  public void setCurrentNoWithdrawal(BigDecimal currentNoWithdrawal) {
    setCurrentNoWithdrawalComm(currentNoWithdrawal);
  }

  @Override
  public Trancd getType() {
    return getTrancd();
  }

  @Override
  public void setType(Trancd type) {
    setTrancd(type);
  }
}
