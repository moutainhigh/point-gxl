package com.hds.xquark.dal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.logging.Logger;

/** @author wangxinhua */
public class PointTotal extends BasePointCommTotal {

  private static final Logger logger = Logger.getLogger(PointTotal.class.getName());

  /** 汉德森可用积分 */
  private BigDecimal usablePointHds;

  /** 汉德森冻结积分 */
  private BigDecimal freezedPointHds;

  private BigDecimal usablePointViviLife;

  private BigDecimal freezedPointViviLife;

  private BigDecimal usablePointEcomm;

  private BigDecimal usablePointPacket = BigDecimal.ZERO;

  private BigDecimal freezedPointEcomm;

  @JsonIgnore
  public BigDecimal getUsablePointHds() {
    return usablePointHds;
  }

  public void setUsablePointHds(BigDecimal usablePointHds) {
    this.usablePointHds = usablePointHds;
  }

  @JsonIgnore
  public BigDecimal getFreezedPointHds() {
    return freezedPointHds;
  }

  public void setFreezedPointHds(BigDecimal freezedPointHds) {
    this.freezedPointHds = freezedPointHds;
  }

  @JsonIgnore
  public BigDecimal getUsablePointViviLife() {
    return usablePointViviLife;
  }

  public void setUsablePointViviLife(BigDecimal usablePointViviLife) {
    this.usablePointViviLife = usablePointViviLife;
  }

  @JsonIgnore
  public BigDecimal getFreezedPointViviLife() {
    return freezedPointViviLife;
  }

  public void setFreezedPointViviLife(BigDecimal freezedPointViviLife) {
    this.freezedPointViviLife = freezedPointViviLife;
  }

  @JsonIgnore
  public BigDecimal getUsablePointEcomm() {
    return usablePointEcomm;
  }

  public void setUsablePointEcomm(BigDecimal usablePointEcomm) {
    this.usablePointEcomm = usablePointEcomm;
  }

  @JsonIgnore
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
  public BigDecimal getNoWithdrawalHds() {
    return BigDecimal.ZERO;
  }

  /**
   * 由于积分/德分共用一套逻辑，德分不能提现，所以为空实现
   * @param noWithdrawalHds 不可提现积分
   */
  @Override
  public void setNoWithdrawalHds(BigDecimal noWithdrawalHds) {
    logger.warning("不支持的德分形式");
  }

  @Override
  public BigDecimal getNoWithdrawalViviLife() {
    return BigDecimal.ZERO;
  }

  @Override
  public void setNoWithdrawalViviLife(BigDecimal noWithdrawalViviLife) {
    logger.warning("不支持的德分形式");
  }

  @Override
  public BigDecimal getNoWithdrawalEcomm() {
    return BigDecimal.ZERO;
  }

  @Override
  public void setNoWithdrawalEcomm(BigDecimal noWithdrawalEcomm) {
    logger.warning("不支持的德分形式");
  }

  /** 德分+红包 */
  public BigDecimal getTotalUsableWithPacket() {
    return getTotalUsable().add(getUsablePointPacket());
  }

  public BigDecimal getUsablePointPacket() {
    return usablePointPacket;
  }

  public void setUsablePointPacket(BigDecimal usablePointPacket) {
    this.usablePointPacket = usablePointPacket;
  }

  @Override
  protected BasePointCommTotal getInstance() {
    return new PointTotal();
  }

  @Override
  public String toString() {
    return "PointTotal{"
        + "usablePointHds="
        + usablePointHds
        + ", freezedPointHds="
        + freezedPointHds
        + ", usablePointViviLife="
        + usablePointViviLife
        + ", freezedPointViviLife="
        + freezedPointViviLife
        + ", usablePointEcomm="
        + usablePointEcomm
        + ", usablePointPacket="
        + usablePointPacket
        + ", freezedPointEcomm="
        + freezedPointEcomm
        + '}';
  }
}
