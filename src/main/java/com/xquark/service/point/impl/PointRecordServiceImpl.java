package com.xquark.service.point.impl;

import com.xquark.dal.mapper.PointRecordMapper;
import com.xquark.dal.model.PointRecord;
import com.xquark.service.point.PointRecordService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * @author wangxinhua on 2018/5/21. DESC: 积分记录service
 */
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
  public List<PointRecord> listByUserId(String userId, Direction direction) {
    return pointRecordMapper.listByUserId(userId, direction);
  }

  @Override
  public List<PointRecord> listByCpId(Long cpId, Direction direction) {
    return pointRecordMapper.listByCpId(cpId, direction);
  }

}
