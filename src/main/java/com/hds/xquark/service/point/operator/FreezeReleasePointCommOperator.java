package com.hds.xquark.service.point.operator;

import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.model.GradeCode;
import com.hds.xquark.dal.model.PointTotal;
import com.hds.xquark.dal.type.CodeNameType;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.service.error.BizException;
import com.hds.xquark.service.error.GlobalErrorCode;
import com.hds.xquark.service.point.PointCommCalResult;
import com.hds.xquark.service.point.helper.PointCommCalHelper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * created by
 *
 * @author wangxinhua at 18-6-18 上午10:27
 */
@Component
public class FreezeReleasePointCommOperator extends BasePointCommOperator {

  @Override
  public PointCommCalResult calRet(PointCommOperatorContext context) {
    BasePointCommTotal infoBefore = context.getTotal();
    // 冻结积分将积分增加到用户冻结积分记录
    BasePointCommTotal infoAfter = PointTotal.copy(infoBefore);
    PlatformType platform = context.getPlatform();
    GradeCode grade = context.getGradeCode();
    // 扣减原冻结积分
    BigDecimal point = grade.getPoint();
    BigDecimal pointFreeze = point.negate();
    BigDecimal oldVal = PointCommCalHelper.getFreezed(infoBefore, platform);
    if (oldVal.compareTo(point) < 0) {
      throw new BizException(GlobalErrorCode.POINT_NOT_ENOUGH, "冻结积分不足以发放");
    }
    // 解冻 增加积分扣减冻结
    PointCommCalHelper.plus(infoAfter, platform, point);
    PointCommCalHelper.plusFreeze(infoAfter, platform, pointFreeze);
    return new PointCommCalResult(infoAfter);
  }

  @Override
  public CodeNameType currType() {
    return CodeNameType.RELEASE;
  }
}
