package com.hds.xquark.dal.model;

import com.hds.xquark.dal.constrant.GradeCodeConstrants;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.Trancd;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/** @author wangxinhua createdAt 18-9-16 下午12:48 */
public abstract class BasePointCommAsst {

  private Long id;
  /** 用户id */
  private Long cpId;
  /** 规则id */
  private Long gradeId;
  /** 功能代码 */
  private String gradeNumber;
  /** 订单号 */
  private String orderId;
  /** 本次积分 */
  private BigDecimal current = BigDecimal.ZERO;
  /** 收入类型，PRBA，CNRA */
  private Trancd trancd;
  /** 积分来源平台 1 hds, 2 vivilife, 3 ecommerce */
  private Integer source;
  /** 当用户发生退货时，会被锁定 */
  private Boolean locked = false;
  /** 是否已回退 */
  private Boolean rollbacked = false;
  /** 回退记录id */
  private Long rollbackId;

  private Date createdAt;
  private Date updatedAt;
  private Boolean isDeleted = false;

  /** build an empty instance of basePointCommAsst */
  public static BasePointCommAsst empty(
      Class<? extends BasePointCommAsst> clazz,
      String orderId,
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
    asst.setOrderId(orderId);
    asst.setGradeId(gradeCode.getId());
    asst.setGradeNumber(gradeCode.getCodeNumber());
    asst.setSource(platform.getCode());
    asst.setTrancd(trancd);
    asst.setCurrent(BigDecimal.ZERO);
    return asst;
  }

  /**
   * 从旧的积分信息上构造一个一样的对象
   *
   * @param oldInfo 旧的数据对象
   * @return 新的积分信息
   */
  public static <T extends BasePointCommAsst> T copy(T oldInfo) {
    @SuppressWarnings("unchecked")
    T newInstance = (T) getInstance(oldInfo.getClass());
    BeanUtils.copyProperties(oldInfo, newInstance);
    return newInstance;
  }

  private static <T extends BasePointCommAsst> T getInstance(Class<T> clazz) {
    T info;
    try {
      info = clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      // class是抽象父类无法实例化或者缺少默认构造函数
      throw new RuntimeException("积分信息构造失败, 请确保class不是是抽象父类" + "且有默认构造函数", e);
    }
    return info;
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

  public boolean isEmpty() {
    return this.getCurrent().signum() == 0;
  }
}
