package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.CustomerWithdrawal;

public interface CustomerWithdrawalMapper {

  int deleteByPrimaryKey(Long id);

  int insert(CustomerWithdrawal record);

  int insertSelective(CustomerWithdrawal record);

  CustomerWithdrawal selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(CustomerWithdrawal record);

  int updateByPrimaryKey(CustomerWithdrawal record);

  boolean isSuspendingExists(Long susId);
}