package com.hds.xquark.dal.model;

import com.hds.xquark.dal.status.PointInfoStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * pointTotalAudit
 *
 * @author
 */
public class PointTotalAudit implements Serializable {

  private static final long serialVersionUID = 1L;
  /** auditId */
  private Long auditId;

  private Long id;
  /** 用户id */
  private Long cpId;
  /** hds总积分 */
  private BigDecimal usablePointHds;
  /** 已冻结积分 */
  private BigDecimal freezedPointHds;
  /** viviLife总积分 */
  private BigDecimal usablePointViviLife;
  /** 已冻结积分 */
  private BigDecimal freezedPointViviLife;
  /** 汉薇商城总积分 */
  private BigDecimal usablePointEcomm;
  /** 已冻结积分 */
  private BigDecimal freezedPointEcomm;
  /** 积分状态 */
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
}
