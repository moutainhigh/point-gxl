package com.hds.xquark.service.point;

import com.hds.xquark.dal.model.GradeCode;
import java.util.List;

/**
 * @author wangxinhua on 2018/5/18. DESC: 积分规则设置service
 */
public interface PointGradeService {

  /**
   * 保存规则
   */
  boolean save(GradeCode gradeCode);

  /**
   * 删除规则
   */
  boolean delete(String id);

  /**
   * 更新规则
   */
  boolean update(GradeCode gradeCode);

  /**
   * 根据id查询规则
   */
  GradeCode load(String id);

  GradeCode loadByFunctionCode(String functionCode);

  GradeCode loadByCategoryAndStatus(Integer categoryId, Integer status);

  List<GradeCode> list(Integer offset, Integer size);

  Long count();

}
