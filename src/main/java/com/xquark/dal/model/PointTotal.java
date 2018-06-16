package com.xquark.dal.model;

import java.math.BigDecimal;

/**
 * @author wangxinhua
 */
public class PointTotal extends BasePointCommTotal {

  /**
   * 汉德森可用积分
   */
  private BigDecimal usablePointHds;

  /**
   * 汉德森冻结积分
   */
  private BigDecimal freezedPointHds;

  private BigDecimal usablePointViviLife;

  private BigDecimal freezedPointViviLife;

  private BigDecimal usablePointEcomm;

  private BigDecimal freezedPointEcomm;

  public BigDecimal getUsablePointHds() {
    return usablePointHds;
  }

  public void setUsablePointHds(BigDecimal usablePointHds) {
    this.usablePointHds = usablePointHds;
  }

  public BigDecimal getFreezedPointHds() {
    return freezedPointHds;
  }

  public void setFreezedPointHds(BigDecimal freezedPointHds) {
    this.freezedPointHds = freezedPointHds;
  }

  public BigDecimal getUsablePointViviLife() {
    return usablePointViviLife;
  }

  public void setUsablePointViviLife(BigDecimal usablePointViviLife) {
    this.usablePointViviLife = usablePointViviLife;
  }

  public BigDecimal getFreezedPointViviLife() {
    return freezedPointViviLife;
  }

  public void setFreezedPointViviLife(BigDecimal freezedPointViviLife) {
    this.freezedPointViviLife = freezedPointViviLife;
  }

  public BigDecimal getUsablePointEcomm() {
    return usablePointEcomm;
  }

  public void setUsablePointEcomm(BigDecimal usablePointEcomm) {
    this.usablePointEcomm = usablePointEcomm;
  }

  public BigDecimal getFreezedPointEcomm() {
    return freezedPointEcomm;
  }

  public void setFreezedPointEcomm(BigDecimal freezedPointEcomm) {
    this.freezedPointEcomm = freezedPointEcomm;
  }

  @Override
  public BigDecimal getUsableHds() {
    return getUsablePointHds();
  }

  @Override
  public void setUsableHds(BigDecimal usableHds) {
    setUsablePointHds(usableHds);
  }

  @Override
  public BigDecimal getUsableViviLife() {
    return getUsablePointViviLife();
  }

  @Override
  public void setUsableViviLife(BigDecimal usableViviLife) {
    setUsablePointViviLife(usableViviLife);
  }

  @Override
  public BigDecimal getUsableEcomm() {
    return getUsablePointEcomm();
  }

  @Override
  public void setUsableEcomm(BigDecimal usableEcomm) {
    setUsablePointEcomm(usableEcomm);
  }

  @Override
  public BigDecimal getFreezedHds() {
    return getFreezedPointHds();
  }

  @Override
  public void setFreezedHds(BigDecimal freezedHds) {
    setFreezedPointHds(freezedHds);
  }

  @Override
  public BigDecimal getFreezedViviLife() {
    return getFreezedPointViviLife();
  }

  @Override
  public void setFreezedViviLife(BigDecimal freezedViviLife) {
    setFreezedPointViviLife(freezedViviLife);
  }

  @Override
  public BigDecimal getFreezedEcomm() {
    return getFreezedPointEcomm();
  }

  @Override
  public void setFreezedEcomm(BigDecimal freezedEcomm) {
    setFreezedPointEcomm(freezedEcomm);
  }

  @Override
  protected BasePointCommTotal getInstance() {
    return new PointTotal();
  }

  @Override
  public String toString() {
    return "PointTotal{" +
        "usablePointHds=" + usablePointHds +
        ", freezedPointHds=" + freezedPointHds +
        ", usablePointViviLife=" + usablePointViviLife +
        ", freezedPointViviLife=" + freezedPointViviLife +
        ", usablePointEcomm=" + usablePointEcomm +
        ", freezedPointEcomm=" + freezedPointEcomm +
        '}';
  }
}