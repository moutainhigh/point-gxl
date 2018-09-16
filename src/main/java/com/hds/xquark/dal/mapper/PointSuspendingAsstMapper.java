package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.PointSuspendingAsst;

public interface PointSuspendingAsstMapper {

  int deleteByPrimaryKey(Long id);

  int insert(PointSuspendingAsst record);

  int insertSelective(PointSuspendingAsst record);

  PointSuspendingAsst selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(PointSuspendingAsst record);

  int updateByPrimaryKey(PointSuspendingAsst record);
}
