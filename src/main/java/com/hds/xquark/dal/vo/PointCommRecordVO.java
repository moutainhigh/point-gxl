package com.hds.xquark.dal.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hds.xquark.dal.type.Trancd;

/**
 * created by
 *
 * @author wangxinhua at 18-6-15 下午5:21
 */
public class PointCommRecordVO {

  private String id;

  /**
   * 用户id
   */
  private Long cpId;

  /**
   * 用户名
   */
  @JsonIgnore
  private String userName;

  /**
   * 规则id
   */
  @JsonIgnore
  private String gradeId;

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

  @JsonIgnore
  private String gradeDesc;

  private Trancd recordType;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getCpId() {
    return cpId;
  }

  public void setCpId(Long cpId) {
    this.cpId = cpId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getGradeId() {
    return gradeId;
  }

  public void setGradeId(String gradeId) {
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

  public String getOrderId() {
    return businessId;
  }

  public void setBusinessId(String businessId) {
    this.businessId = businessId;
  }

  public Boolean getRollbacked() {
    return rollbacked;
  }

  public void setRollbacked(Boolean rollbacked) {
    this.rollbacked = rollbacked;
  }

  public String getGradeDesc() {
    return gradeDesc;
  }

  public void setGradeDesc(String gradeDesc) {
    this.gradeDesc = gradeDesc;
  }

  public Trancd getRecordType() {
    return recordType;
  }

  public void setRecordType(Trancd recordType) {
    this.recordType = recordType;
  }

  public String getRecordDesc() {
    return getGradeDesc();
  }

  public Integer getSource() {
    return source;
  }

  public void setSource(Integer source) {
    this.source = source;
  }
}
