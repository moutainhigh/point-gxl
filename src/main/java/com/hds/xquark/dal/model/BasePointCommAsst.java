package com.hds.xquark.dal.model;

import com.hds.xquark.dal.constrant.GradeCodeConstrants;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.Trancd;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * @author wangxinhua createdAt 18-9-16 下午12:48
 */
public abstract class BasePointCommAsst {

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
  private String gradeNumber;
  /**
   * 订单号
   */
  private String orderId;
  /**
   * 本次积分
   */
  private BigDecimal current;
  /**
   * 收入类型，PRBA，CNRA
   */
  private Trancd trancd;
  /**
   * 积分来源平台 1 hds, 2 vivilife, 3 ecommerce
   */
  private Integer source;
  /**
   * 当用户发生退货时，会被锁定
   */
  private Boolean locked;
  /**
   * 是否已回退
   */
  private Boolean rollbacked;
  /**
   * 回退记录id
   */
  private Long rollbackId;

  private Date createdAt;
  private Date updatedAt;
  private Boolean isDeleted;

  /**
   * build an empty instance of basePointCommAsst
   */
  public static BasePointCommAsst empty(
      Class<? extends BasePointCommAsst> clazz,
      Long cpId,
      GradeCode gradeCode,
      PlatformType platform,
      Trancd trancd) {
    BasePointCommAsst asst;
    try {
      asst = clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new IllegalStateException(clazz.getSimpleName() + "cant be instanced");
    }
    asst.setCpId(cpId);
    asst.setGradeId(gradeCode.getId());
    asst.setGradeNumber(gradeCode.getCodeNumber());
    asst.setSource(platform.getCode());
    asst.setTrancd(trancd);
    asst.setCurrent(BigDecimal.ZERO);
    return asst;
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

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
  }

  public String getGradeNumber() {
    return gradeNumber;
  }

  public void setGradeNumber(String gradeNumber) {
    this.gradeNumber = gradeNumber;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public BigDecimal getCurrent() {
    return current;
  }

  public void setCurrent(BigDecimal current) {
    this.current = current;
  }

  public Trancd getTrancd() {
    return trancd;
  }

  public void setTrancd(Trancd trancd) {
    this.trancd = trancd;
  }

  public Integer getSource() {
    return source;
  }

  public void setSource(Integer source) {
    this.source = source;
  }

  public Boolean getLocked() {
    return locked;
  }

  public void setLocked(Boolean locked) {
    this.locked = locked;
  }

  public Boolean getRollbacked() {
    return rollbacked;
  }

  public void setRollbacked(Boolean rollbacked) {
    this.rollbacked = rollbacked;
  }

  public Long getRollbackId() {
    return rollbackId;
  }

  public void setRollbackId(Long rollbackId) {
    this.rollbackId = rollbackId;
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

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean deleted) {
    isDeleted = deleted;
  }

  /**
   * add record to this asst only support consume record yet
   *
   * @param record record to merge
   * @return this instance
   */
  public BasePointCommAsst addRecord(BasePointCommRecord record) {
    if (record == null) {
      // do nothing
      return this;
    }
    String gradeCode = record.getCodeNumber();
    if (!GradeCodeConstrants.CONSUME_CODE.contains(gradeCode)) {
      throw new IllegalArgumentException("asst operation not allowed");
    }
    if (!Objects.equals(this.getSource(), record.getSource())) {
      throw new IllegalArgumentException("asst source must be same with record");
    }
    BigDecimal curr = this.getCurrent();
    this.setCurrent(curr.add(record.getCurrent()));
    return this;
  }
}
