package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.PointRecord;
import com.hds.xquark.dal.type.PointRecordType;
import com.hds.xquark.dal.vo.PointRecordVO;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public interface PointRecordMapper {

  int deleteByPrimaryKey(String id);

  int insert(PointRecord record);

  PointRecord selectByPrimaryKey(String id);

  int updateByPrimaryKeySelective(PointRecord record);

  int updateByPrimaryKey(PointRecord record);

  List<PointRecord> listByUserId(@Param("userId") String userId,
      @Param("direction") Direction direction);

  List<PointRecord> listByCpId(@Param("cpId") Long cpId,
      @Param("direction") Direction direction);

  List<PointRecordVO> listVO(@Param("cpId") Long cpId, @Param("source") Integer source,
      @Param("page") Pageable pageable);

  Long count(@Param("cpId") Long cpId, @Param("source") Integer sourceCode);

  PointRecord loadUnRollBackedByUserIdWithBizId(@Param("bizId") String bizId, @Param("userId") String userId);

  PointRecord loadUnRollBackedByCpIdWithBizId(@Param("bizId") String bizId,
      @Param("cpId") Long cpId);

  List<PointRecord> listUnRollBackedByCpIdWithBizIdAndType(@Param("bizId") String bizId,
      @Param("cpId") Long cpId, @Param("type") PointRecordType type);

  PointRecord loadUnRollBackedByUserIdWithBizIdAndFCode(@Param("bizId") String bizId,
      @Param("userId") String userId, @Param("functionCode") String functionCode);

  List<PointRecord> listFreezedRecordAfterDate(@Param("date") Date date);

  List<PointRecord> listUnFreezedRecord();
}