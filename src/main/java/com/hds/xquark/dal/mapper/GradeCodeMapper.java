package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.GradeCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GradeCodeMapper {

  int deleteByPrimaryKey(String id);

  int insert(GradeCode record);

  boolean selectFunctionCodeExists(String functionCode);

  GradeCode selectByPrimaryKey(String id);

  GradeCode selectByFunctionCode(String functionCode);

  GradeCode selectByCategoryIdAndStatus(
      @Param("categoryId") Integer categoryId, @Param("status") Integer status);

  List<GradeCode> list(@Param("offset") Integer offset, @Param("size") Integer size);

  Long count();

  int updateByPrimaryKeySelective(GradeCode record);

  int updateByPrimaryKeyWithBLOBs(GradeCode record);
}
