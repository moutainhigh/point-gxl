package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.CommissionSuspendingAsst;

public interface CommissionSuspendingAsstMapper {

  int deleteByPrimaryKey(Long id);

  int insert(CommissionSuspendingAsst record);

  int insertSelective(CommissionSuspendingAsst record);

  CommissionSuspendingAsst selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(CommissionSuspendingAsst record);

  int updateByPrimaryKey(CommissionSuspendingAsst record);
}
