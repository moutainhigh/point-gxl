package com.hds.xquark.dal.mapper;

import com.hds.xquark.dal.model.CommissionRecord;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.dal.vo.CommissionRecordVO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CommissionRecordMapper {

  int deleteByPrimaryKey(Long id);

  int insert(CommissionRecord record);

  CommissionRecord selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(CommissionRecord record);

  int updateByPrimaryKey(CommissionRecord record);

  List<CommissionRecord> listUnRollBackedByCpIdWithBizIdAndType(@Param("bizId") String bizId,
      @Param("cpId") Long cpId, @Param("type") Trancd type);

  List<CommissionRecord> listByTimeRange(@Param("start") Date start, @Param("end") Date end,
      @Param("grade") String grade, @Param("source") Integer source);

  boolean selectRecordExists(
      @Param("bizId") String bizId, @Param("cpId") Long cpId, @Param("type") Trancd type);

  List<CommissionRecordVO> listVO(@Param("cpId") Long cpId, @Param("source") Integer source,
      @Param("offset") Integer offset, @Param("size") Integer size);

  List<CommissionRecordVO> listVoAsst(@Param("cpId") Long cpId, @Param("source") Integer source,
      @Param("offset") Integer offset, @Param("size") Integer size);

  Long count(@Param("cpId") Long cpId, @Param("source") Integer source);

  List<CommissionRecord> listFreezedRecordAfterDate(@Param("date") Date date);

  List<CommissionRecord> listUnFreezedRecord();

  BigDecimal sumTotal(@Param("code") String code, @Param("cpId") Long cpId,
      @Param("trancd") Trancd trancd);

}