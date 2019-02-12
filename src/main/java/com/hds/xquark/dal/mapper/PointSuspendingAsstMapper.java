package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.PointSuspendingAsst;
import com.hds.xquark.dal.type.Trancd;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PointSuspendingAsstMapper {

  int deleteByPrimaryKey(Long id);

  int insert(PointSuspendingAsst record);

  int insertSelective(PointSuspendingAsst record);

  PointSuspendingAsst selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(PointSuspendingAsst record);

  int updateByPrimaryKey(PointSuspendingAsst record);

  List<PointSuspendingAsst> listAsst(
      @Param("orderId") String orderId, @Param("cpId") Long cpId, @Param("trancd") Trancd trancd);
}
