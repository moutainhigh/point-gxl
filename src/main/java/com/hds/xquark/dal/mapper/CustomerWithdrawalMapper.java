package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.CustomerWithdrawal;
import com.hds.xquark.dal.vo.CommissionWithdrawVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerWithdrawalMapper {

  int deleteByPrimaryKey(Long id);

  int insert(CustomerWithdrawal record);

  int insertSelective(CustomerWithdrawal record);

  CustomerWithdrawal selectByPrimaryKey(Long id);

  List<CustomerWithdrawal> listByCpIdAndMonth(
      @Param("cpId") Long cpId, @Param("month") Integer month, @Param("source") Integer source);

  boolean selectIsWithdrawExists(
      @Param("cpId") Long cpId, @Param("month") Integer month, @Param("source") Integer source);

  int updateByPrimaryKeySelective(CustomerWithdrawal record);

  int updateByMonthSelective(CustomerWithdrawal record);

  int updateByPrimaryKey(CustomerWithdrawal record);

  boolean isSuspendingExists(Long susId);

  /** 查询所有银行的提现记录 */
  List<CommissionWithdrawVO> listWithdraw(
      @Param("orderMonth") Integer orderMonth, @Param("source") Integer source);

  /** 查询中行的提现记录 */
  List<CommissionWithdrawVO> listZHWithdraw(
      @Param("orderMonth") Integer orderMonth, @Param("source") Integer source);

  /** 查询非中行的提现记录 */
  List<CommissionWithdrawVO> listNonZHWithdraw(
      @Param("orderMonth") Integer orderMonth, @Param("source") Integer source);

  /**
   * 获取最近的可展示月份
   *
   * @param month 取几个月
   * @param source 平台
   * @return 月份集合
   */
  List<String> listTopDate(@Param("month") int month, @Param("source") Integer source);

  boolean selectIsCpIdWithdrawed(@Param("cpId") Long cpId, @Param("month") Integer month);

  boolean selectOrderMonthProcessed(@Param("month") Integer month);
}
