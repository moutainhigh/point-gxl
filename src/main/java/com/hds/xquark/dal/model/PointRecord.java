package com.hds.xquark.dal.model;

import com.hds.xquark.dal.type.Trancd;

import java.math.BigDecimal;

/** @author wangxinhua */
public class PointRecord extends BasePointCommRecord {

  /** 本次积分 */
  private BigDecimal currentPoint;

  /** 本次冻结积分 */
  private BigDecimal currentFreezedPoint;

  /** 本次不可提现积分 */
  private BigDecimal currentNoWithdrawalComm;

  /** 收入类型 */
  private Trancd trancd;

  public BigDecimal getCurrentPoint() {
    return currentPoint;
  }

  public void setCurrentPoint(BigDecimal currentPoint) {
    this.currentPoint = currentPoint;
  }

  public BigDecimal getCurrentFreezedPoint() {
    return currentFreezedPoint;
  }

  public void setCurrentFreezedPoint(BigDecimal currentFreezedPoint) {
    this.currentFreezedPoint = currentFreezedPoint;
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
    return getCurrentPoint();
  }

  @Override
  public void setCurrent(BigDecimal current) {
    setCurrentPoint(current);
  }

  @Override
  public BigDecimal getCurrentFreezed() {
    return getCurrentFreezedPoint();
  }

  @Override
  public void setCurrentFreezed(BigDecimal currentFreezed) {
    setCurrentFreezedPoint(currentFreezed);
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
