package com.xquark.dal.mapper;

import com.xquark.dal.model.CommissionRecord;
import com.xquark.dal.type.PointRecordType;
import com.xquark.dal.vo.CommissionRecordVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

public interface CommissionRecordMapper {

  int deleteByPrimaryKey(Long id);

  int insert(CommissionRecord record);

  CommissionRecord selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(CommissionRecord record);

  int updateByPrimaryKey(CommissionRecord record);

  List<CommissionRecord> listUnRollBackedByCpIdWithBizIdAndType(@Param("bizId") String bizId,
      @Param("cpId") Long cpId, @Param("type") PointRecordType type);

  List<CommissionRecordVO> listVO(@Param("cpId") Long cpId, @Param("source") Integer source,
      @Param("page") Pageable pageable);

  Long count(@Param("cpId") Long cpId, @Param("source") Integer source);

}