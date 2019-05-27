package com.hds.xquark.dal.model;

import com.hds.xquark.dal.status.DepositStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/** @author wangxinhua */
public class CustomerWithdrawal implements Serializable {

  private static final long serialVersionUID = 1L;
  /** id */
  private Long id;
  /** commissionSuspendingId */
  private Long commsuspendingId;
  /** 分享人ID */
  private Long cpId;
  /** 提现时间 */
  private Date withdrawDate;
  /** 金额 */
  private BigDecimal amount;
  /** 来源平台 1 hds, 2 vivilife, 3 ecommerce */
  private Integer source;
  /** 201809/201810 */
  private Integer processingMonth;

  private Date createdDate;
  /** 0 表示还未发放，1 表示落账成功, 4 表示落账失败 */
  private Integer depositStatus = DepositStatus.CREATED.getCode();

  private Date updatedDate;
  /** 落账失败原因 */
  private String remark;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCommsuspendingId() {
    return commsuspendingId;
  }

  public void setCommsuspendingId(Long commsuspendingId) {
    this.commsuspendingId = commsuspendingId;
  }

  public Long getCpId() {
    return cpId;
  }

  public void setCpId(Long cpId) {
    this.cpId = cpId;
  }

  public Date getWithdrawDate() {
    return withdrawDate;
  }

  public void setWithdrawDate(Date withdrawDate) {
    this.withdrawDate = withdrawDate;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Integer getSource() {
    return source;
  }

  public void setSource(Integer source) {
    this.source = source;
  }

  public Integer getProcessingMonth() {
    return processingMonth;
  }

  public void setProcessingMonth(Integer processingMonth) {
    this.processingMonth = processingMonth;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Integer getDepositStatus() {
    return depositStatus;
  }

  public void setDepositStatus(Integer depositStatus) {
    this.depositStatus = depositStatus;
  }

  public Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
