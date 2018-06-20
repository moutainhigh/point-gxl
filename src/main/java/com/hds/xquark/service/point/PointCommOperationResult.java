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

/**
 * @author wangxinhua on 2018/5/21. DESC: 积分操作计算结果
 */
public class PointCommOperationResult {

  /**
   * 规则id
   */
  private Long gradeId;

  /**
   * 修改用户id
   */
  private Long cpId;

  /**
   * 当此修改积分数量
   */
  private BigDecimal currentPoint;

  /**
   * 积分修改平台信息
   */
  private PlatformType platform;

  /**
   * 修改后积分积分信息
   */
  private BasePointCommTotal infoBefore;

  /**
   * 修改后的积分信息
   */
  private BasePointCommTotal infoAfter;

  @JsonIgnore
  private List<? extends BasePointCommRecord> currRecords;

  @JsonIgnore
  private List<BasePointCommRecord> rollBacked;

  @JsonIgnore
  private Trancd trancd;

  @JsonIgnore
  private Map<PlatformType, BigDecimal> detailMap;

  /**
   * 使用的规则类型
   */
  private CodeNameType usingGradeType;

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

  public BigDecimal getCurrentPoint() {
    return currentPoint;
  }

  public void setCurrentPoint(BigDecimal currentPoint) {
    this.currentPoint = currentPoint;
  }

  public PlatformType getPlatform() {
    return platform;
  }

  public void setPlatform(PlatformType platform) {
    this.platform = platform;
  }

  public BasePointCommTotal getInfoAfter() {
    return infoAfter;
  }

  public void setInfoAfter(BasePointCommTotal infoAfter) {
    this.infoAfter = infoAfter;
  }

  public BasePointCommTotal getInfoBefore() {
    return infoBefore;
  }

  public void setInfoBefore(BasePointCommTotal infoBefore) {
    this.infoBefore = infoBefore;
  }

  public List<BasePointCommRecord> getRollBacked() {
    return rollBacked;
  }

  public void setRollBacked(List<BasePointCommRecord> rollBacked) {
    this.rollBacked = rollBacked;
  }

  public CodeNameType getUsingGradeType() {
    return usingGradeType;
  }

  public void setUsingGradeType(CodeNameType usingGradeType) {
    this.usingGradeType = usingGradeType;
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

  public List<? extends BasePointCommRecord> getCurrRecords() {
    return currRecords;
  }

  public void setCurrRecords(List<? extends BasePointCommRecord> currRecords) {
    this.currRecords = currRecords;
  }
}
