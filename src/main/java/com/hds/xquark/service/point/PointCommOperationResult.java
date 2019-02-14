package com.hds.xquark.service.point;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hds.xquark.dal.model.BasePointCommRecord;
import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.type.CodeNameType;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.Trancd;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** @author wangxinhua on 2018/5/21. DESC: 积分操作计算结果 */
public class PointCommOperationResult<T extends BasePointCommTotal, R extends BasePointCommRecord> {

  /** 规则id */
  private Long gradeId;

  /** 修改用户id */
  private Long cpId;

  /** 当此修改积分数量 */
  private BigDecimal currentModified;

  /** 积分修改平台信息 */
  private PlatformType platform;

  /** 修改后积分积分信息 */
  private T infoBefore;

  /** 修改后的积分信息 */
  private T infoAfter;

  private List<R> currRecords;

  @JsonIgnore private List<R> rollBacked;

  @JsonIgnore private Trancd trancd;

  @JsonIgnore private Map<PlatformType, BigDecimal> detailMap;

  /** 使用的规则类型 */
  private CodeNameType usingGradeType;

  /** 是否可提现 */
  private Integer usedType;

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
  }

  public Long getCpId() {
    return cpId;
  }

  public void setCpId(Long cpId) {
    this.cpId = cpId;
  }

  public BigDecimal getCurrentModified() {
    return currentModified;
  }

  public void setCurrentModified(BigDecimal currentModified) {
    this.currentModified = currentModified;
  }

  public PlatformType getPlatform() {
    return platform;
  }

  public void setPlatform(PlatformType platform) {
    this.platform = platform;
  }

  public T getInfoBefore() {
    return infoBefore;
  }

  public void setInfoBefore(T infoBefore) {
    this.infoBefore = infoBefore;
  }

  public T getInfoAfter() {
    return infoAfter;
  }

  public void setInfoAfter(T infoAfter) {
    this.infoAfter = infoAfter;
  }

  public List<R> getCurrRecords() {
    return currRecords;
  }

  public void setCurrRecords(List<R> currRecords) {
    this.currRecords = currRecords;
  }

  public List<R> getRollBacked() {
    return rollBacked;
  }

  public void setRollBacked(List<R> rollBacked) {
    this.rollBacked = rollBacked;
  }

  public Trancd getTrancd() {
    return trancd;
  }

  public void setTrancd(Trancd trancd) {
    this.trancd = trancd;
  }

  public Map<PlatformType, BigDecimal> getDetailMap() {
    return detailMap;
  }

  public void setDetailMap(Map<PlatformType, BigDecimal> detailMap) {
    this.detailMap = detailMap;
  }

  public CodeNameType getUsingGradeType() {
    return usingGradeType;
  }

  public void setUsingGradeType(CodeNameType usingGradeType) {
    this.usingGradeType = usingGradeType;
  }

  public Integer getUsedType() {
    return usedType;
  }

  public void setUsedType(Integer usedType) {
    this.usedType = usedType;
  }
}
