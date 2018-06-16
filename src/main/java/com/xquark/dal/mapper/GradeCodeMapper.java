package com.xquark.dal.mapper;

import com.xquark.dal.model.GradeCode;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

public interface GradeCodeMapper {

  int deleteByPrimaryKey(String id);

  int insert(GradeCode record);

  boolean selectFunctionCodeExists(String functionCode);

  GradeCode selectByPrimaryKey(String id);

  GradeCode selectByFunctionCode(String functionCode);

  List<GradeCode> list(@Param("page") Pageable pageable);

  Long count();

  int updateByPrimaryKeySelective(GradeCode record);

  int updateByPrimaryKeyWithBLOBs(GradeCode record);
}