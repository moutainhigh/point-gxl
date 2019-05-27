package com.hds.xquark.dal.constrant;

import java.math.BigDecimal;

/**
 * created by
 *
 * @author wangxinhua at 18-6-17 下午3:21
 */
public class PointConstrants {

  public static final int POINT_CATEGORY = 100;

  public static final int COMMISSION_CATEGORY = 200;

  public static final BigDecimal POINT_TO_COMM_RATE = BigDecimal.valueOf(10);

  public static final BigDecimal COMM_TO_POINT_RATE = BigDecimal.valueOf(0.1);

  /** 积分提现下限 */
  public static final BigDecimal WITH_DRAW_LIMIT = BigDecimal.valueOf(200);
}
