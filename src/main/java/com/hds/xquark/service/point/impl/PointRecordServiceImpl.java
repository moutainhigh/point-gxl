package com.hds.xquark.service.point.impl;

import com.hds.xquark.dal.mapper.PointRecordMapper;
import com.hds.xquark.dal.model.PointRecord;
import com.hds.xquark.service.point.PointRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** @author wangxinhua on 2018/5/21. DESC: 积分记录service */
@Service
public class PointRecordServiceImpl implements PointRecordService {

  private PointRecordMapper pointRecordMapper;

  @Autowired
  public PointRecordServiceImpl(PointRecordMapper pointRecordMapper) {
    this.pointRecordMapper = pointRecordMapper;
  }

  @Override
  public boolean save(PointRecord record) {
    return pointRecordMapper.insert(record) > 0;
  }

  @Override
  public List<PointRecord> listByUserId(String userId, String direction) {
    return pointRecordMapper.listByUserId(userId, direction);
  }

  @Override
  public List<PointRecord> listByCpId(Long cpId, String direction) {
    return pointRecordMapper.listByCpId(cpId, direction);
  }
}
