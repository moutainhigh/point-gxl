package com.hds.xquark.service.point.operator;

import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.type.CodeNameType;
import com.hds.xquark.service.point.PointCommCalResult;
import com.hds.xquark.service.point.helper.PointCommCalHelper;
import org.springframework.stereotype.Component;

/**
 * @author wangxinhua on 2018/5/21. DESC: 增加积分操作
 */
@Component
public class GrantPointCommOperator extends BasePointCommOperator {

  @Override
  public PointCommCalResult calRet(PointCommOperatorContext context) {
    BasePointCommTotal infoBefore = context.getTotal();
    BasePointCommTotal infoAfter = BasePointCommTotal.copy(infoBefore);
    PointCommCalHelper.plus(infoAfter, context.getPlatform(), context.
        getGradeCode().getPoint());
    return new PointCommCalResult(infoAfter);
  }

  @Override
  public CodeNameType currType() {
    return CodeNameType.GRANT;
  }

}
