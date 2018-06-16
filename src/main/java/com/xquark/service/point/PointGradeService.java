package com.xquark.service.point;

import com.xquark.dal.model.GradeCode;
import java.util.List;
import org.springframework.data.domain.Pageable;

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

  List<GradeCode> list(Pageable pageable);

  Long count();

}
