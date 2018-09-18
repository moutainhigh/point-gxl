package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.CommissionSuspendingAsst;
import com.hds.xquark.dal.type.Trancd;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CommissionSuspendingAsstMapper {

  int deleteByPrimaryKey(Long id);

  int insert(CommissionSuspendingAsst record);

  int insertSelective(CommissionSuspendingAsst record);

  CommissionSuspendingAsst selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(CommissionSuspendingAsst record);

  int updateByPrimaryKey(CommissionSuspendingAsst record);

  List<CommissionSuspendingAsst> listAsst(@Param("orderId") String orderId,
      @Param("cpId") Long cpId, @Param("trancd") Trancd trancd);
}
