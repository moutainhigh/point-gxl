package com.xquark.service.point.impl;

import com.xquark.dal.mapper.GradeCodeMapper;
import com.xquark.dal.model.GradeCode;
import com.xquark.service.error.BizException;
import com.xquark.service.error.GlobalErrorCode;
import com.xquark.service.point.PointGradeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author wangxinhua on 2018/5/18. DESC:
 */
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
  public List<GradeCode> list(Pageable pageable) {
    return gradeCodeMapper.list(pageable);
  }

  @Override
  public Long count() {
    return gradeCodeMapper.count();
  }
}