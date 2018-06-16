package com.xquark.dal.mapper;

import com.xquark.dal.model.PointTotal;

public interface PointTotalMapper {

  int deleteByPrimaryKey(String id);

  int insert(PointTotal record);

  PointTotal selectByPrimaryKey(Long id);

  PointTotal selectByCpId(Long cpId);

  int updateByPrimaryKeySelective(PointTotal record);

}