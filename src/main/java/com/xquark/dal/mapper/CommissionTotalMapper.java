package com.xquark.dal.mapper;

import com.xquark.dal.model.CommissionTotal;

public interface CommissionTotalMapper {

  int deleteByPrimaryKey(Long id);

  int insert(CommissionTotal record);

  CommissionTotal selectByPrimaryKey(Long id);

  CommissionTotal selectByCpId(Long id);

  int updateByPrimaryKeySelective(CommissionTotal record);

  int updateByPrimaryKey(CommissionTotal record);
}