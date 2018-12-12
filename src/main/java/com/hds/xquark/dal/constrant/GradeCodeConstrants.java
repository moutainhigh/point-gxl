package com.hds.xquark.dal.constrant;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

/**
 * created by
 *
 * @author wangxinhua at 18-6-18 上午10:53
 * @deprecated using {@link com.hds.xquark.service.point.type.FunctionCodeType instead}
 */
public class GradeCodeConstrants {

  public static final String GRANT_POINT_CODE = "1001";

  public static final String CONSUME_POINT_CODE = "1002";

  public static final String FREEZE_GRANT_POINT_CODE = "1003";

  public static final String CANCEL_POINT_CODE = "1004";

  public static final String RETURN_POINT_CODE = "1005";

  public static final String RELEASE_POINT_CODE = "1006";

  /**
   * 消费到德分红包
   */
  public static final String CONSUME_PACKET_POINT_CODE = "1007";

  /**
   * 德分红包回退
   */
  public static final String RETURN_PACKET_POINT_CODE = "1008";

  public static final String GRANT_COMMISSION_CODE = "2001";

  public static final String CONSUME_COMMISSION_CODE = "2002";

  public static final String FREEZE_GRANT_COMMISSION_CODE = "2003";

  public static final String CANCEL_COMMISSION_CODE = "2004";

  public static final String RETURN_COMMISSION_CODE = "2005";

  public static final String RELEASE_COMMISSION_CODE = "2006";

  /**
   * 提现积分
   */
  public static final String WITH_DRAW_COMMISSION_CODE = "2007";

  /**
   * combine point and commission consume to a set
   */
  public static final Set<String> CONSUME_CODE = ImmutableSet
      .of(CONSUME_COMMISSION_CODE, CONSUME_POINT_CODE);

}
