package com.hds.xquark.service.point.operator;

import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.model.GradeCode;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.PointOperateType;
import com.hds.xquark.dal.type.PointRecordType;

/**
 * @author wangxinhua on 2018/5/21. DESC:
 */
public class PointCommOperatorContext {

  /**
   * 当前操作的用户id
   */
  private final Long cpId;

  /**
   * 用户积分信息
   */
  private final BasePointCommTotal total;

  /**
   * 使用的积分规则
   */
  private final GradeCode gradeCode;

  /**
   * 业务id
   */
  private final String businessId;

  private final PlatformType platform;

  private final PointOperateType operateType;

  private Long points;

  private PointRecordType recordType;

  PointCommOperatorContext(Long cpId, BasePointCommTotal total,
      GradeCode gradeCode, String businessId, PlatformType platform,
      PointOperateType operateType) {
    this.cpId = cpId;
    this.total = total;
    this.gradeCode = gradeCode;
    this.businessId = businessId;
    this.platform = platform;
    this.operateType = operateType;
  }

  public Long getCpId() {
    return cpId;
  }

  public BasePointCommTotal getTotal() {
    return total;
  }

  public GradeCode getGradeCode() {
    return gradeCode;
  }

  public String getBusinessId() {
    return businessId;
  }

  public PlatformType getPlatform() {
    return platform;
  }

  public PointOperateType getOperateType() {
    return operateType;
  }

  public Long getPoints() {
    return points;
  }

  public void setPoints(Long points) {
    this.points = points;
  }

  public PointRecordType getRecordType() {
    return recordType;
  }

  public void setRecordType(PointRecordType recordType) {
    this.recordType = recordType;
  }
}
