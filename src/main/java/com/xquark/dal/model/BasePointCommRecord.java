package com.xquark.dal.model;

import com.xquark.dal.type.PointRecordType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * created by
 *
 * @author wangxinhua at 18-6-15 上午11:48
 */
public abstract class BasePointCommRecord {

  private Long id;

  /**
   * 用户id
   */
  private Long cpId;

  /**
   * 规则id
   */
  private Long gradeId;

  /**
   * 功能代码
   */
  private String codeNumber;

  /**
   * 业务id
   */
  private String businessId;

  /**
   * 积分来源平台
   */
  private Integer source;

  private Boolean rollbacked;

  /**
   * 解冻记录id
   */
  private Long unFreezeId;

  /**
   * 回滚记录id
   */
  private Long rollbackId;

  /**
   * 冻结时间开始时间
   */
  private Date freezedAt;

  /**
   * 冻结结束时间
   */
  private Date freezedTo;

  private Date createdAt;

  private Date updatedAt;

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

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
  }

  public String getCodeNumber() {
    return codeNumber;
  }

  public void setCodeNumber(String codeNumber) {
    this.codeNumber = codeNumber;
  }

  public String getBusinessId() {
    return businessId;
  }

  public void setBusinessId(String businessId) {
    this.businessId = businessId;
  }

  public Integer getSource() {
    return source;
  }

  public void setSource(Integer source) {
    this.source = source;
  }

  public Boolean getRollbacked() {
    return rollbacked;
  }

  public void setRollbacked(Boolean rollbacked) {
    this.rollbacked = rollbacked;
  }

  public Long getUnFreezeId() {
    return unFreezeId;
  }

  public void setUnFreezeId(Long unFreezeId) {
    this.unFreezeId = unFreezeId;
  }

  public Long getRollbackId() {
    return rollbackId;
  }

  public void setRollbackId(Long rollbackId) {
    this.rollbackId = rollbackId;
  }

  public Date getFreezedAt() {
    return freezedAt;
  }

  public void setFreezedAt(Date freezedAt) {
    this.freezedAt = freezedAt;
  }

  public Date getFreezedTo() {
    return freezedTo;
  }

  public void setFreezedTo(Date freezedTo) {
    this.freezedTo = freezedTo;
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

  /**
   * 本次操作积分/德分
   */
  public abstract BigDecimal getCurrent();

  /**
   * 本次操作冻结积分/德分
   */
  public abstract BigDecimal getCurrentFreezed();

  /**
   * 设置积分/德分
   */
  public abstract void setCurrent(BigDecimal current);

  /**
   * 设置冻结积分/德分
   */
  public abstract void setCurrentFreezed(BigDecimal currentFreezed);

  public abstract PointRecordType getType();

  public abstract void setType(PointRecordType type);

}
