package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.PointRecord;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.dal.vo.PointRecordVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface PointRecordMapper {

  int deleteByPrimaryKey(String id);

  int insert(PointRecord record);

  PointRecord selectByPrimaryKey(String id);

  int updateByPrimaryKeySelective(PointRecord record);

  int updateByPrimaryKey(PointRecord record);

  List<PointRecord> listByUserId(
      @Param("userId") String userId, @Param("direction") String direction);

  List<PointRecord> listByCpId(@Param("cpId") Long cpId, @Param("direction") String direction);

  List<PointRecordVO> listVO(
      @Param("cpId") Long cpId,
      @Param("source") Integer source,
      @Param("offset") Integer offset,
      @Param("size") Integer size);

  /** 新的查询, 连接asst, 查询多条记录 */
  List<PointRecordVO> listVOAsst(
      @Param("cpId") Long cpId,
      @Param("source") Integer source,
      @Param("offset") Integer offset,
      @Param("size") Integer size);

  Long count(@Param("cpId") Long cpId, @Param("source") Integer sourceCode);

  PointRecord loadUnRollBackedByUserIdWithBizId(
      @Param("bizId") String bizId, @Param("userId") String userId);

  PointRecord loadUnRollBackedByCpIdWithBizId(
      @Param("bizId") String bizId, @Param("cpId") Long cpId);

  List<PointRecord> listUnRollBackedByCpIdWithBizIdAndType(
      @Param("bizId") String bizId, @Param("cpId") Long cpId, @Param("type") Trancd type);

  boolean selectRecordExists(
      @Param("bizId") String bizId, @Param("cpId") Long cpId, @Param("type") Trancd type);

  PointRecord loadUnRollBackedByUserIdWithBizIdAndFCode(
      @Param("bizId") String bizId,
      @Param("userId") String userId,
      @Param("functionCode") String functionCode);

  List<PointRecord> listFreezedRecordAfterDate(@Param("date") Date date);

  List<PointRecord> listUnFreezedRecord();

  BigDecimal sumByTrancd(@Param("cpId") Long cpId, @Param("trancd") Trancd trancd);
}
