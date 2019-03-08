package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.CommissionTotalAudit;

public interface CommissionTotalAuditMapper {

  int deleteByPrimaryKey(Long auditId);

  int insert(CommissionTotalAudit record);

  CommissionTotalAudit selectByPrimaryKey(Long auditId);

  int updateByPrimaryKeySelective(CommissionTotalAudit record);

  int updateByPrimaryKey(CommissionTotalAudit record);
}
