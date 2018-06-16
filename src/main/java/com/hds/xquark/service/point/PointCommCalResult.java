package com.hds.xquark.service.point;

import com.hds.xquark.dal.model.BasePointCommRecord;
import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.type.PlatformType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author wangxinhua on 2018/5/22. DESC: 封装积分计算结果
 */
public class PointCommCalResult {

  public PointCommCalResult(BasePointCommTotal infoAfter) {
    this(infoAfter, null, null);
  }

  public PointCommCalResult(BasePointCommTotal infoAfter,
      Map<PlatformType, BigDecimal> detailMap) {
    this(infoAfter, null, detailMap);
  }

  public PointCommCalResult(BasePointCommTotal infoAfter,
      List<BasePointCommRecord> rollBacked) {
    this(infoAfter, rollBacked, null);
  }

  public PointCommCalResult(BasePointCommTotal infoAfter, List<BasePointCommRecord> rollBacked,
      Map<PlatformType, BigDecimal> detailMap) {
    this.infoAfter = infoAfter;
    this.rollBacked = rollBacked;
    this.detailMap = detailMap;
  }

  /**
   * 修改的积分数, 可以为正或负, 有延时处理积分的业务(如冻结x天后解冻)因此修改积分跟用户变更积分分开处理
   */
//  private final Integer modifiedPoints;

  /**
   * 修改后的用户积分信息
   */
  private final BasePointCommTotal infoAfter;

  private Map<PlatformType, BigDecimal> detailMap;

  /**
   * 回滚记录
   */
  private List<BasePointCommRecord> rollBacked;

  public BasePointCommTotal getInfoAfter() {
    return infoAfter;
  }

  public List<BasePointCommRecord> getRollBacked() {
    return rollBacked;
  }

  public void setRollBacked(List<BasePointCommRecord> rollBacked) {
    this.rollBacked = rollBacked;
  }

  public Map<PlatformType, BigDecimal> getDetailMap() {
    return detailMap;
  }

  public void setDetailMap(Map<PlatformType, BigDecimal> detailMap) {
    this.detailMap = detailMap;
  }
}
