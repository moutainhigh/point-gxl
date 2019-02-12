package com.hds.xquark.dal.model;

import com.hds.xquark.dal.type.CodeNameType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/** @author wangxinhua */
public class GradeCode implements Serializable {

  private static final long serialVersionUID = 1L;
  /** id */
  private Long id;
  /** 功能类型,比如message/point/commission */
  private Long categoryId;
  /** 原functionCode */
  private String codeNumber;
  /** 积分类型 GRANT - 发放, CONSUME - 消费, ROLLBACK - 回退, FREEZE - 冻结 */
  private CodeNameType codeName;

  private BigDecimal point;
  private Date createdDate;
  /** 描述 */
  private String description;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public CodeNameType getCodeName() {
    return codeName;
  }

  public void setCodeName(CodeNameType codeName) {
    this.codeName = codeName;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCodeNumber() {
    return codeNumber;
  }

  public void setCodeNumber(String codeNumber) {
    this.codeNumber = codeNumber;
  }

  // 兼容原functionCode
  public String getFunctionCode() {
    return codeNumber;
  }

  public void setFunctionCode(String codeNumber) {
    this.codeNumber = codeNumber;
  }

  public BigDecimal getPoint() {
    return point;
  }

  public void setPoint(BigDecimal point) {
    this.point = point;
  }
}
