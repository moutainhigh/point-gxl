package com.hds.xquark.service.point;

import com.hds.xquark.dal.model.PointRecord;

import java.util.List;

/** @author wangxinhua on 2018/5/21. DESC: 积分记录 */
public interface PointRecordService {

  /**
   * 保存积分记录
   *
   * @param record
   */
  boolean save(PointRecord record);

  /** 根据用户id查询积分明细 */
  List<PointRecord> listByUserId(String userId, String direction);

  /** 根据用户unionId查询积分明细 */
  List<PointRecord> listByCpId(Long cpId, String direction);
}
