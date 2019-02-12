package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.PointTotalAudit;

public interface PointTotalAuditMapper {

  int deleteByPrimaryKey(Long auditId);

  int insert(PointTotalAudit record);

  PointTotalAudit selectByPrimaryKey(Long auditId);

  int updateByPrimaryKeySelective(PointTotalAudit record);

  int updateByPrimaryKey(PointTotalAudit record);
}
