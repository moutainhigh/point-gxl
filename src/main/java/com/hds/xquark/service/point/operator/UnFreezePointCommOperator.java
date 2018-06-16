package com.hds.xquark.service.point.operator;

import com.hds.xquark.dal.type.CodeNameType;

/**
 * @author wangxinhua on 2018/5/29. DESC: 解除冻结积分, 复用增加积分规则
 */
public class UnFreezePointCommOperator extends GrantPointCommOperator {

  @Override
  public CodeNameType currType() {
    return CodeNameType.UNFREEZE;
  }

}
