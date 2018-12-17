package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.CommissionTotal;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Param;

public interface CommissionTotalMapper {

  int deleteByPrimaryKey(Long id);

  int insert(CommissionTotal record);

  CommissionTotal selectByPrimaryKey(Long id);

  CommissionTotal selectByCpId(Long id);

  int updateByPrimaryKeySelective(CommissionTotal record);

  int updateByPrimaryKey(CommissionTotal record);

  int updateByCpId(CommissionTotal record);

  void grantWithProcedure(
      @Param("cpId") Long cpId, @Param("source") int source, @Param("val") BigDecimal val,
      @Param("trancd") String trancd, @Param("desc") String desc);
}