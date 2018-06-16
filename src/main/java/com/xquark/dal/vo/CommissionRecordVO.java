package com.xquark.dal.vo;

/**
 * created by
 *
 * @author wangxinhua at 18-6-15 下午4:23
 */
public class CommissionRecordVO extends PointCommRecordVO {


  /**
   * 本次积分
   */
  private Long currentCommission;

  /**
   * 本次冻结积分
   */
  private Long currentFreezedCommission;

  public Long getCurrentCommission() {
    return currentCommission;
  }

  public void setCurrentCommission(Long currentCommission) {
    this.currentCommission = currentCommission;
  }

  public Long getCurrentFreezedCommission() {
    return currentFreezedCommission;
  }

  public void setCurrentFreezedCommission(Long currentFreezedCommission) {
    this.currentFreezedCommission = currentFreezedCommission;
  }
}
