package com.hds.xquark.dal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

/**
 * commissionTotal
 *
 * @author wangxinhua
 */
public class CommissionTotal extends BasePointCommTotal {

  /** 汉德森可用积分 */
  private BigDecimal usableCommHds;

  /** 汉德森冻结积分 */
  private BigDecimal freezedCommHds;

  /** 汉德森不可提现积分 */
  private BigDecimal noWithdrawalCommHds;

  private BigDecimal usableCommViviLife;

  private BigDecimal freezedCommViviLife;

  private BigDecimal noWithdrawalCommViviLife;

  private BigDecimal usableCommEcomm;

  private BigDecimal freezedCommEcomm;

  private BigDecimal noWithdrawalCommEcomm;

  @JsonIgnore
  public BigDecimal getUsableCommHds() {
    return usableCommHds;
  }

  public void setUsableCommHds(BigDecimal usableCommHds) {
    this.usableCommHds = usableCommHds;
  }

  @JsonIgnore
  public BigDecimal getFreezedCommHds() {
    return freezedCommHds;
  }

  public void setFreezedCommHds(BigDecimal freezedCommHds) {
    this.freezedCommHds = freezedCommHds;
  }

  @JsonIgnore
  public BigDecimal getUsableCommViviLife() {
    return usableCommViviLife;
  }

  public void setUsableCommViviLife(BigDecimal usableCommViviLife) {
    this.usableCommViviLife = usableCommViviLife;
  }

  @JsonIgnore
  public BigDecimal getFreezedCommViviLife() {
    return freezedCommViviLife;
  }

  public void setFreezedCommViviLife(BigDecimal freezedCommViviLife) {
    this.freezedCommViviLife = freezedCommViviLife;
  }

  @JsonIgnore
  public BigDecimal getUsableCommEcomm() {
    return usableCommEcomm;
  }

  public void setUsableCommEcomm(BigDecimal usableCommEcomm) {
    this.usableCommEcomm = usableCommEcomm;
  }

  @JsonIgnore
  public BigDecimal getFreezedCommEcomm() {
    return freezedCommEcomm;
  }

  public void setFreezedCommEcomm(BigDecimal freezedCommEcomm) {
    this.freezedCommEcomm = freezedCommEcomm;
  }

  @JsonIgnore
  public BigDecimal getNoWithdrawalCommHds() {
    return noWithdrawalCommHds;
  }

  public void setNoWithdrawalCommHds(BigDecimal noWithdrawalCommHds) {
    this.noWithdrawalCommHds = noWithdrawalCommHds;
  }

  @JsonIgnore
  public BigDecimal getNoWithdrawalCommViviLife() {
    return noWithdrawalCommViviLife;
  }

  public void setNoWithdrawalCommViviLife(BigDecimal noWithdrawalCommViviLife) {
    this.noWithdrawalCommViviLife = noWithdrawalCommViviLife;
  }

  @JsonIgnore
  public BigDecimal getNoWithdrawalCommEcomm() {
    return noWithdrawalCommEcomm;
  }

  public void setNoWithdrawalCommEcomm(BigDecimal noWithdrawalCommEcomm) {
    this.noWithdrawalCommEcomm = noWithdrawalCommEcomm;
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
  public BigDecimal getNoWithdrawalHds() {
    return getNoWithdrawalCommHds();
  }

  @Override
  public void setNoWithdrawalHds(BigDecimal noWithdrawalHds) {
    setNoWithdrawalCommHds(noWithdrawalHds);
  }

  @Override
  public BigDecimal getNoWithdrawalViviLife() {
    return getNoWithdrawalCommViviLife();
  }

  @Override
  public void setNoWithdrawalViviLife(BigDecimal noWithdrawalViviLife) {
    setNoWithdrawalCommViviLife(noWithdrawalViviLife);
  }

  @Override
  public BigDecimal getNoWithdrawalEcomm() {
    return getNoWithdrawalCommEcomm();
  }

  @Override
  public void setNoWithdrawalEcomm(BigDecimal noWithdrawalEcomm) {
    setNoWithdrawalCommEcomm(noWithdrawalEcomm);
  }

  @Override
  protected BasePointCommTotal getInstance() {
    return new CommissionTotal();
  }

  @Override
  public String toString() {
    return "CommissionTotal{"
        + "usableCommHds="
        + usableCommHds
        + ", freezedCommHds="
        + freezedCommHds
        + ", noWithdrawalCommHds="
        + noWithdrawalCommHds
        + ", usableCommViviLife="
        + usableCommViviLife
        + ", freezedCommViviLife="
        + freezedCommViviLife
        + ", noWithdrawalCommViviLife="
        + noWithdrawalCommViviLife
        + ", usableCommEcomm="
        + usableCommEcomm
        + ", freezedCommEcomm="
        + freezedCommEcomm
        + ", noWithdrawalCommEcomm="
        + noWithdrawalCommEcomm
        + '}';
  }
}
