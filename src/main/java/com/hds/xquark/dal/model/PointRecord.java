package com.hds.xquark.dal.model;

import com.hds.xquark.dal.type.Trancd;
import java.math.BigDecimal;

/**
 * @author wangxinhua
 */
public class PointRecord extends BasePointCommRecord {

  /**
   * 本次积分
   */
  private BigDecimal currentPoint;

  /**
   * 本次冻结积分
   */
  private BigDecimal currentFreezedPoint;

  /**
   * 收入类型
   */
  private Trancd trancd;

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


  public Trancd getTrancd() {
    return trancd;
  }

  public void setTrancd(Trancd trancd) {
    this.trancd = trancd;
  }

  @Override
  public BigDecimal getCurrent() {
    return getCurrentPoint();
  }

  @Override
  public BigDecimal getCurrentFreezed() {
    return getCurrentFreezedPoint();
  }

  @Override
  public void setCurrent(BigDecimal current) {
    setCurrentPoint(current);
  }

  @Override
  public void setCurrentFreezed(BigDecimal currentFreezed) {
    setCurrentFreezedPoint(currentFreezed);
  }

  @Override
  public Trancd getType() {
    return getTrancd();
  }

  @Override
  public void setType(Trancd type) {
    setTrancd(type);
  }

}