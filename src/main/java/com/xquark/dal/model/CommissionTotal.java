package com.xquark.dal.model;

import java.math.BigDecimal;

/**
 * commissionTotal
 *
 * @author wangxinhua
 */
public class CommissionTotal extends BasePointCommTotal {

  /**
   * 汉德森可用积分
   */
  private BigDecimal usableCommHds;

  /**
   * 汉德森冻结积分
   */
  private BigDecimal freezedCommHds;

  private BigDecimal usableCommViviLife;

  private BigDecimal freezedCommViviLife;

  private BigDecimal usableCommEcomm;

  private BigDecimal freezedCommEcomm;

  public BigDecimal getUsableCommHds() {
    return usableCommHds;
  }

  public void setUsableCommHds(BigDecimal usableCommHds) {
    this.usableCommHds = usableCommHds;
  }

  public BigDecimal getFreezedCommHds() {
    return freezedCommHds;
  }

  public void setFreezedCommHds(BigDecimal freezedCommHds) {
    this.freezedCommHds = freezedCommHds;
  }

  public BigDecimal getUsableCommViviLife() {
    return usableCommViviLife;
  }

  public void setUsableCommViviLife(BigDecimal usableCommViviLife) {
    this.usableCommViviLife = usableCommViviLife;
  }

  public BigDecimal getFreezedCommViviLife() {
    return freezedCommViviLife;
  }

  public void setFreezedCommViviLife(BigDecimal freezedCommViviLife) {
    this.freezedCommViviLife = freezedCommViviLife;
  }

  public BigDecimal getUsableCommEcomm() {
    return usableCommEcomm;
  }

  public void setUsableCommEcomm(BigDecimal usableCommEcomm) {
    this.usableCommEcomm = usableCommEcomm;
  }

  public BigDecimal getFreezedCommEcomm() {
    return freezedCommEcomm;
  }

  public void setFreezedCommEcomm(BigDecimal freezedCommEcomm) {
    this.freezedCommEcomm = freezedCommEcomm;
  }

  @Override
  public BigDecimal getUsableHds() {
    return getUsableCommHds();
  }

  @Override
  public void setUsableHds(BigDecimal usableHds) {
    setUsableCommHds(usableHds);
  }

  @Override
  public BigDecimal getUsableViviLife() {
    return getUsableCommViviLife();
  }

  @Override
  public void setUsableViviLife(BigDecimal usableViviLife) {
    setUsableCommViviLife(usableViviLife);
  }

  @Override
  public BigDecimal getUsableEcomm() {
    return getUsableCommEcomm();
  }

  @Override
  public void setUsableEcomm(BigDecimal usableEcomm) {
    setUsableCommEcomm(usableEcomm);
  }

  @Override
  public BigDecimal getFreezedHds() {
    return getFreezedCommHds();
  }

  @Override
  public void setFreezedHds(BigDecimal freezedHds) {
    setFreezedCommHds(freezedHds);
  }

  @Override
  public BigDecimal getFreezedViviLife() {
    return getFreezedCommViviLife();
  }

  @Override
  public void setFreezedViviLife(BigDecimal freezedViviLife) {
    setFreezedCommViviLife(freezedViviLife);
  }

  @Override
  public BigDecimal getFreezedEcomm() {
    return getFreezedCommEcomm();
  }

  @Override
  public void setFreezedEcomm(BigDecimal freezedEcomm) {
    setFreezedCommEcomm(freezedEcomm);
  }

  @Override
  protected BasePointCommTotal getInstance() {
    return new CommissionTotal();
  }

  @Override
  public String toString() {
    return "CommissionTotal{" +
        "usableCommHds=" + usableCommHds +
        ", freezedCommHds=" + freezedCommHds +
        ", usableCommViviLife=" + usableCommViviLife +
        ", freezedCommViviLife=" + freezedCommViviLife +
        ", usableCommEcomm=" + usableCommEcomm +
        ", freezedCommEcomm=" + freezedCommEcomm +
        '}';
  }
}