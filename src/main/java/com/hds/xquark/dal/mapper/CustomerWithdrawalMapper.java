package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.CustomerWithdrawal;
import com.hds.xquark.dal.vo.CommissionWithdrawVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CustomerWithdrawalMapper {

  int deleteByPrimaryKey(Long id);

  int insert(CustomerWithdrawal record);

  int insertSelective(CustomerWithdrawal record);

  CustomerWithdrawal selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(CustomerWithdrawal record);

  int updateByPrimaryKey(CustomerWithdrawal record);

  boolean isSuspendingExists(Long susId);

  /**
   * 查询所有银行的提现记录
   */
  List<CommissionWithdrawVO> listWithdraw(@Param("orderMonth") Integer orderMonth,
      @Param("source") Integer source);

  /**
   * 查询中行的提现记录
   */
  List<CommissionWithdrawVO> listZHWithdraw(@Param("orderMonth") Integer orderMonth,
      @Param("source") Integer source);

  /**
   * 查询非中行的提现记录
   */
  List<CommissionWithdrawVO> listNonZHWithdraw(@Param("orderMonth") Integer orderMonth,
      @Param("source") Integer source);
}