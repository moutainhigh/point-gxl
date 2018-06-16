package com.xquark.service.point.operator;

import com.xquark.dal.model.BasePointCommRecord;
import com.xquark.dal.model.BasePointCommTotal;
import com.xquark.dal.model.GradeCode;
import com.xquark.dal.model.PointTotal;
import com.xquark.dal.type.CodeNameType;
import com.xquark.dal.type.PlatformType;
import com.xquark.dal.type.PointRecordType;
import com.xquark.service.point.PointCommCalResult;
import com.xquark.service.point.PointCommOperationResult;
import com.xquark.service.point.helper.PointCommCalHelper;
import com.xquark.utils.DateUtils;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author wangxinhua on 2018/5/21. DESC: 增加积分并立即冻结
 */
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
  public boolean saveBackRecord(String bizId, GradeCode grade, PointCommOperationResult calRet,
      Class<? extends BasePointCommRecord> clazz) {
    // 将积分记录设置为已冻结
    BasePointCommRecord record = buildRecord(bizId, grade, calRet,
        PointRecordType.FREEZE, clazz);

    // 将冻结积分数目保存到本次冻结字段
    // 以免统计时将冻结积分统计到总积分
    // 解冻时将该字段取出增加到用户可用积分
    record.setCurrentFreezed(grade.getPoint());
    Date now = new Date();

    Date freezedTo = DateUtils.addDate(now, null, null, 7);
    record.setFreezedAt(now);
    record.setFreezedTo(freezedTo);
    return saveRecord(record);
  }

}
