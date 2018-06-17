package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.GradeCode;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GradeCodeMapper {

  int deleteByPrimaryKey(String id);

  int insert(GradeCode record);

  boolean selectFunctionCodeExists(String functionCode);

  GradeCode selectByPrimaryKey(String id);

  GradeCode selectByFunctionCode(String functionCode);

  List<GradeCode> list(@Param("offset") Integer offset, @Param("size") Integer size);

  Long count();

  int updateByPrimaryKeySelective(GradeCode record);

  int updateByPrimaryKeyWithBLOBs(GradeCode record);
}