package com.hds.xquark.dal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.Trancd;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * created by
 *
 * @author wangxinhua at 18-6-15 上午11:48
 */
public abstract class BasePointCommRecord {

  private Long id;

  /** 用户id */
  private Long cpId;

  /** 规则id */
  private Long gradeId;

  /** 功能代码 */
  private String codeNumber;

  /** 业务id */
  private String businessId;

  /** 积分来源平台 */
  private Integer source;

  private Integer belongingTo;

  /** 是否可提现类型 */
  private Integer usedType;

  private Boolean rollbacked;

  /** 解冻记录id */
  private Long unFreezeId;

  /** 回滚记录id */
  private Long rollbackId;

  /** 冻结时间开始时间 */
  @JsonIgnore private Date freezedAt;

  /** 冻结结束时间 */
  @JsonIgnore private Date freezedTo;

  private Date createdAt;

  @JsonIgnore private Date updatedAt;

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

  @JsonIgnore
  public String getBusinessId() {
    return businessId;
  }

  public void setBusinessId(String businessId) {
    this.businessId = businessId;
  }

  public String getOrderId() {
    return businessId;
  }

  public Integer getSource() {
    return source;
  }

  public void setSource(Integer source) {
    this.source = source;
  }

  public PlatformType getPlatForm() {
    return PlatformType.fromCode(source);
  }

  public Integer getBelongingTo() {
    return belongingTo;
  }

  public void setBelongingTo(Integer belongingTo) {
    this.belongingTo = belongingTo;
  }

  public Boolean getRollbacked() {
    return rollbacked;
  }

  public void setRollbacked(Boolean rollbacked) {
    this.rollbacked = rollbacked;
  }

  public Integer getUsedType() {
    return usedType;
  }

  public void setUsedType(Integer usedType) {
    this.usedType = usedType;
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

  /** 本次操作积分/德分 */
  public abstract BigDecimal getCurrent();

  /** 设置积分/德分 */
  public abstract void setCurrent(BigDecimal current);

  /** 本次操作冻结积分/德分 */
  public abstract BigDecimal getCurrentFreezed();

  /** 设置冻结积分/德分 */
  public abstract void setCurrentFreezed(BigDecimal currentFreezed);

  /** 本次操作不可提现积分 */
  public abstract BigDecimal getCurrentNoWithdrawal();

  /** 设置不可提现积分 */
  public abstract void setCurrentNoWithdrawal(BigDecimal currentNoWithdrawal);

/*  *//** 获取提现状态 *//*
  public abstract Integer getUsedTypeC();

  *//** 设置提现状态 *//*
  public abstract void setUsedTypeC(Integer usedType);*/

  public abstract Trancd getType();

  public abstract void setType(Trancd type);

  /**
   * 从旧的积分信息上构造一个一样的对象
   *
   * @param oldInfo 旧的数据对象
   * @return 新的积分信息
   */
  public static <T extends BasePointCommRecord> T copy(T oldInfo) {
    @SuppressWarnings("unchecked")
    T newInstance = (T) getInstance(oldInfo.getClass());
    BeanUtils.copyProperties(oldInfo, newInstance);
    return newInstance;
  }

  private static <T extends BasePointCommRecord> T getInstance(Class<T> clazz) {
    T info;
    try {
      info = clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      // class是抽象父类无法实例化或者缺少默认构造函数
      throw new RuntimeException("积分信息构造失败, 请确保class不是是抽象父类" + "且有默认构造函数", e);
    }
    return info;
  }
}
