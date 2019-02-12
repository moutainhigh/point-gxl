package com.hds.xquark.service.point.impl;

import com.hds.xquark.dal.mapper.GradeCodeMapper;
import com.hds.xquark.dal.model.GradeCode;
import com.hds.xquark.service.error.BizException;
import com.hds.xquark.service.error.GlobalErrorCode;
import com.hds.xquark.service.point.PointGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** @author wangxinhua on 2018/5/18. DESC: */
@Service
public class PointGradeServiceImpl implements PointGradeService {

  private final GradeCodeMapper gradeCodeMapper;

  @Autowired
  public PointGradeServiceImpl(GradeCodeMapper gradeCodeMapper) {
    this.gradeCodeMapper = gradeCodeMapper;
  }

  @Override
  public boolean save(GradeCode gradeCode) {
    String functionCode = gradeCode.getFunctionCode();
    if (gradeCodeMapper.selectFunctionCodeExists(functionCode)) {
      throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "规则代码已存在");
    }
    return gradeCodeMapper.insert(gradeCode) > 0;
  }

  @Override
  public boolean delete(String id) {
    return gradeCodeMapper.deleteByPrimaryKey(id) > 0;
  }

  @Override
  public boolean update(GradeCode gradeCode) {
    return gradeCodeMapper.updateByPrimaryKeySelective(gradeCode) > 0;
  }

  @Override
  public GradeCode load(String id) {
    return gradeCodeMapper.selectByPrimaryKey(id);
  }

  @Override
  public GradeCode loadByFunctionCode(String functionCode) {
    return gradeCodeMapper.selectByFunctionCode(functionCode);
  }

  @Override
  public GradeCode loadByCategoryAndStatus(Integer categoryId, Integer status) {
    return gradeCodeMapper.selectByCategoryIdAndStatus(categoryId, status);
  }

  @Override
  public List<GradeCode> list(Integer offset, Integer size) {
    return gradeCodeMapper.list(offset, size);
  }

  @Override
  public Long count() {
    return gradeCodeMapper.count();
  }
}
