package com.hds.xquark.service.point;

import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.model.CommissionTotal;
import com.hds.xquark.dal.model.PointTotal;
import com.hds.xquark.dal.type.PlatformType;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.data.domain.Pageable;

/**
 * @author wangxinhua on 2018/5/18. DESC: 积分信息service
 */
public interface PointCommService {

  /**
   * 根据积分规则处理积分
   *
   * @param bizId 业务id
   * @param funcCode 规则代码
   * @param platform 所属平台
   * @param points 改动积分数，若规则本身设置了静态积分则不生效
   * @return {@link PointCommOperationResult} 积分处理结果
   */
  PointCommOperationResult modifyPoint(Long cpId, String bizId,
      String funcCode, PlatformType platform, BigDecimal points);

  /**
   * 根据德分规则处理积分
   *
   * @param bizId 业务id
   * @param funcCode 规则代码
   * @param platform 所属平台
   * @param commission 改动积分数，若规则本身设置了静态积分则不生效
   * @return {@link PointCommOperationResult} 积分处理结果
   */
  PointCommOperationResult modifyCommission(Long cpId, String bizId,
      String funcCode, PlatformType platform, BigDecimal commission);

  /**
   * 更新或新增用户积分信息
   *
   * @param info 积分对象
   * @return true or false
   */
  boolean saveOrUpdate(BasePointCommTotal info);

  PointTotal loadByCpId(Long cpId);

  CommissionTotal loadCommByCpId(Long cpId);

  /**
   * 获取用户最后更新的积分记录, 若不存在则返回一个空数据
   *
   * @param cpId 用户id
   * @param clazz 积分/德分类型
   * @return 积分信息, 不能为空
   */
  @SuppressWarnings("unchecked")
  <T extends BasePointCommTotal> T loadOrBuildInfo(Long cpId, Class<T> clazz);

  Map<String, Object> listPointRecords(Long cpId, Integer code, Pageable pageable);

  Map<String, Object> listCommissionRecords(Long cpId, Integer code, Pageable pageable);
}
