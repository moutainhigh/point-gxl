package com.hds.xquark.dal.model;

import com.hds.xquark.dal.type.Trancd;
import java.math.BigDecimal;

/**
 * commissionsuspending
 *
 * @author wangxinhua
 */
public class CommissionRecord extends BasePointCommRecord {

  /**
   * 本次佣金
   */
  private BigDecimal currentComm;

  /**
   * 本次冻结佣金
   */
  private BigDecimal currentFreezedComm;

  private Trancd commType;

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

  public Trancd getCommType() {
    return commType;
  }

  public void setCommType(Trancd commType) {
    this.commType = commType;
  }

  @Override
  public BigDecimal getCurrent() {
    return getCurrentComm();
  }

  @Override
  public BigDecimal getCurrentFreezed() {
    return getCurrentFreezedComm();
  }

  @Override
  public void setCurrent(BigDecimal current) {
    setCurrentComm(current);
  }

  @Override
  public void setCurrentFreezed(BigDecimal currentFreezed) {
    setCurrentFreezedComm(currentFreezed);
  }

  @Override
  public Trancd getType() {
    return getCommType();
  }

  @Override
  public void setType(Trancd type) {
    setCommType(type);
  }
}