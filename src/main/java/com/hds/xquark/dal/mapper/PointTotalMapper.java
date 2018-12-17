package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.PointTotal;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Param;

public interface PointTotalMapper {

  int deleteByPrimaryKey(String id);

  int insert(PointTotal record);

  PointTotal selectByPrimaryKey(Long id);

  PointTotal selectByCpId(Long cpId);

  int updateByPrimaryKeySelective(PointTotal record);

  int updateByCpId(PointTotal record);

  void grantWithProcedure(
      @Param("cpId") Long cpId, @Param("source") int source, @Param("val") BigDecimal val,
      @Param("trancd") String trancd, @Param("desc") String desc);

}