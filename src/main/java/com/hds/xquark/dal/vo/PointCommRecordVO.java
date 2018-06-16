package com.hds.xquark.dal.vo;

import com.hds.xquark.dal.type.PointRecordType;

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
  private String userName;

  /**
   * 规则id
   */
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

  private String gradeDesc;

  private PointRecordType recordType;

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

  public String getBusinessId() {
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

  public PointRecordType getRecordType() {
    return recordType;
  }

  public void setRecordType(PointRecordType recordType) {
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
