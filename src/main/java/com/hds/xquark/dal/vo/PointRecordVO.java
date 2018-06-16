package com.hds.xquark.dal.vo;

/**
 * @author wangxinhua on 2018/5/21. DESC:
 */
public class PointRecordVO extends PointCommRecordVO {

  /**
   * 本次积分
   */
  private Long currentPoint;

  /**
   * 本次冻结积分
   */
  private Long currentFreezedPoint;

  public Long getCurrentPoint() {
    return currentPoint;
  }

  public void setCurrentPoint(Long currentPoint) {
    this.currentPoint = currentPoint;
  }

  public Long getCurrentFreezedPoint() {
    return currentFreezedPoint;
  }

  public void setCurrentFreezedPoint(Long currentFreezedPoint) {
    this.currentFreezedPoint = currentFreezedPoint;
  }

}
