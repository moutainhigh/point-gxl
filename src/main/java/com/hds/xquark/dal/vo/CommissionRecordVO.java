package com.hds.xquark.dal.vo;

import com.hds.xquark.dal.constrant.GradeCodeConstrants;
import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;

/**
 * created by
 *
 * @author wangxinhua at 18-6-15 下午4:23
 */
public class CommissionRecordVO extends PointCommRecordVO {


  /**
   * 本次积分
   */
  private BigDecimal currentCommission;

  /**
   * 本次冻结积分
   */
  private BigDecimal currentFreezedCommission;

  public BigDecimal getCurrentCommission() {
    return currentCommission;
  }

  public void setCurrentCommission(BigDecimal currentCommission) {
    this.currentCommission = currentCommission;
  }

  public BigDecimal getCurrentFreezedCommission() {
    return currentFreezedCommission;
  }

  public void setCurrentFreezedCommission(BigDecimal currentFreezedCommission) {
    this.currentFreezedCommission = currentFreezedCommission;
  }

  public BigDecimal getCurrentStr() {
    if (getCurrentCommission().signum() == 0) {
      return getCurrentFreezedCommission();
    }
    if (getCurrentFreezedCommission().signum() == 0) {
      return getCurrentCommission();
    }
    return getCurrentCommission();
  }
}
