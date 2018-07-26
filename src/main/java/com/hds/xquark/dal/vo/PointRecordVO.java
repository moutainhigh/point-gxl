package com.hds.xquark.dal.vo;

import com.hds.xquark.dal.constrant.GradeCodeConstrants;
import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangxinhua on 2018/5/21. DESC:
 */
public class PointRecordVO extends PointCommRecordVO {

  /**
   * 本次积分
   */
  private BigDecimal currentPoint;

  /**
   * 本次冻结积分
   */
  private BigDecimal currentFreezedPoint;

  public BigDecimal getCurrentPoint() {
    return currentPoint;
  }

  public void setCurrentPoint(BigDecimal currentPoint) {
    this.currentPoint = currentPoint;
  }

  public BigDecimal getCurrentFreezedPoint() {
    return currentFreezedPoint;
  }

  public void setCurrentFreezedPoint(BigDecimal currentFreezedPoint) {
    this.currentFreezedPoint = currentFreezedPoint;
  }

  public BigDecimal getCurrentStr() {
    if (getCurrentPoint().signum() == 0) {
      return getCurrentFreezedPoint();
    }
    if (getCurrentFreezedPoint().signum() == 0) {
      return getCurrentPoint();
    }
    return getCurrentPoint();
  }

}
