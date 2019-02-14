package com.hds.xquark.dal.model;

import com.hds.xquark.dal.status.PointInfoStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * commissiontotalaudit
 *
 * @author
 */
public class CommissionTotalAudit implements Serializable {

  private static final long serialVersionUID = 1L;
  /** auditId */
  private Long auditId;

  private Long id;
  /** 用户id */
  private Long cpId;
  /** hds总佣金 */
  private BigDecimal usableCommHds;
  /** 已冻结佣金 */
  private BigDecimal freezedCommHds;
  /** 汉德森不可提现积分 */
  private BigDecimal nowithdrawalCommHds;
  /** viviLife总佣金 */
  private BigDecimal usableCommViviLife;
  /** 已冻结佣金 */
  private BigDecimal freezedCommViviLife;
  /** viviLife不可提现积分 */
  private BigDecimal nowithdrawalCommViviLife;
  /** 汉薇商城总佣金 */
  private BigDecimal usableCommEcomm;
  /** 已冻结佣金 */
  private BigDecimal freezedCommEcomm;
  /** 汉薇商城不可提现积分 */
  private BigDecimal nowithdrawalCommEcomm;
  /** 佣金状态 */
  private PointInfoStatus status;

  private Date createdAt;
  private Date updatedAt;
  private Boolean archive;
  /** 1 insert, 2 update, 3 delete */
  private Integer auditType;
  /** H -> HDS, V -> vivilife, E -> ecommerce, S -> system */
  private String auditUser;

  private Date auditDate;

  public Long getAuditId() {
    return auditId;
  }

  public void setAuditId(Long auditId) {
    this.auditId = auditId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCpId() {
    return cpId;
  }

  public void setCpId(Long cpId) {
    this.cpId = cpId;
  }

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

  public BigDecimal getNowithdrawalCommHds() {
    return nowithdrawalCommHds;
  }

  public void setNowithdrawalCommHds(BigDecimal nowithdrawalCommHds) {
    this.nowithdrawalCommHds = nowithdrawalCommHds;
  }

  public BigDecimal getNowithdrawalCommViviLife() {
    return nowithdrawalCommViviLife;
  }

  public void setNowithdrawalCommViviLife(BigDecimal nowithdrawalCommViviLife) {
    this.nowithdrawalCommViviLife = nowithdrawalCommViviLife;
  }

  public BigDecimal getNowithdrawalCommEcomm() {
    return nowithdrawalCommEcomm;
  }

  public void setNowithdrawalCommEcomm(BigDecimal nowithdrawalCommEcomm) {
    this.nowithdrawalCommEcomm = nowithdrawalCommEcomm;
  }

  public PointInfoStatus getStatus() {
    return status;
  }

  public void setStatus(PointInfoStatus status) {
    this.status = status;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Boolean getArchive() {
    return archive;
  }

  public void setArchive(Boolean archive) {
    this.archive = archive;
  }

  public Integer getAuditType() {
    return auditType;
  }

  public void setAuditType(Integer auditType) {
    this.auditType = auditType;
  }

  public String getAuditUser() {
    return auditUser;
  }

  public void setAuditUser(String auditUser) {
    this.auditUser = auditUser;
  }

  public Date getAuditDate() {
    return auditDate;
  }

  public void setAuditDate(Date auditDate) {
    this.auditDate = auditDate;
  }
}
