package com.hds.xquark.service.point.operator;

import com.hds.xquark.dal.model.BasePointCommRecord;
import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.model.GradeCode;
import com.hds.xquark.dal.model.PointTotal;
import com.hds.xquark.dal.type.CodeNameType;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.service.point.PointCommCalResult;
import com.hds.xquark.service.point.PointCommOperationResult;
import com.hds.xquark.service.point.helper.PointCommCalHelper;
import com.hds.xquark.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/** @author wangxinhua on 2018/5/21. DESC: 增加积分并立即冻结 */
@Component
public class FreezeGrantPointCommOperator extends BasePointCommOperator {

  @Override
  public PointCommCalResult calRet(PointCommOperatorContext context) {
    // 冻结积分将积分增加到用户冻结积分记录
    BasePointCommTotal infoAfter = PointTotal.copy(context.getTotal());
    PlatformType platform = context.getPlatform();
    GradeCode grade = context.getGradeCode();
    PointCommCalHelper.plusFreeze(infoAfter, platform, grade.getPoint());
    return new PointCommCalResult(infoAfter);
  }

  @Override
  public CodeNameType currType() {
    return CodeNameType.FREEZE_GRANT;
  }

  @Override
  public List<? extends BasePointCommRecord> saveBackRecord(
      String bizId,
      GradeCode grade,
      PointCommOperationResult calRet,
      Class<? extends BasePointCommRecord> clazz) {
    // 将积分记录设置为已冻结
    BasePointCommRecord record = buildRecord(bizId, grade, calRet, calRet.getTrancd(), clazz);

    // 将冻结积分数目保存到本次冻结字段
    // 以免统计时将冻结积分统计到总积分
    // 解冻时将该字段取出增加到用户可用积分
    record.setCurrentFreezed(grade.getPoint());
    record.setRollbacked(false);
    Date now = new Date();

    // 汉薇14天其他平台7天
    int freezeDays = calRet.getPlatform() == PlatformType.E ? 14 : 7;
    Date freezedTo = DateUtils.addDate(now, null, null, freezeDays);
    record.setFreezedAt(now);
    record.setFreezedTo(freezedTo);
    saveRecord(record);
    return Collections.singletonList(record);
  }
}
