package com.xquark.service.point.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.xquark.dal.mapper.CommissionTotalMapper;
import com.xquark.dal.mapper.PointTotalMapper;
import com.xquark.dal.model.BasePointCommRecord;
import com.xquark.dal.model.BasePointCommTotal;
import com.xquark.dal.model.CommissionTotal;
import com.xquark.dal.model.GradeCode;
import com.xquark.dal.model.PointTotal;
import com.xquark.dal.type.CodeNameType;
import com.xquark.dal.type.PlatformType;
import com.xquark.dal.type.PointOperateType;
import com.xquark.service.error.BizException;
import com.xquark.service.error.GlobalErrorCode;
import com.xquark.service.point.PointCommOperationResult;
import com.xquark.service.point.PointCommService;
import com.xquark.service.point.PointGradeService;
import com.xquark.service.point.operator.BasePointCommOperator;
import com.xquark.service.point.operator.PointOperatorFactory;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangxinhua on 2018/5/21. DESC:
 */
@Service
public class PointCommServiceImpl implements PointCommService {

  private PointGradeService pointGradeService;

  private PointTotalMapper pointTotalMapper;

  private CommissionTotalMapper commissionTotalMapper;

  @Autowired
  public void setPointGradeService(PointGradeService pointGradeService) {
    this.pointGradeService = pointGradeService;
  }

  @Autowired
  public void setPointTotalMapper(PointTotalMapper pointTotalMapper) {
    this.pointTotalMapper = pointTotalMapper;
  }

  @Autowired
  public void setCommissionTotalMapper(CommissionTotalMapper commissionTotalMapper) {
    this.commissionTotalMapper = commissionTotalMapper;
  }

  @Override
  public PointCommOperationResult modifyPoint(Long cpId, String bizId,
      String funcCode, PlatformType platform, BigDecimal points) {
    return modifyPointComm(cpId, bizId, funcCode, platform, points, PointOperateType.POINT);
  }

  @Override
  public PointCommOperationResult modifyCommission(Long cpId, String bizId,
      String funcCode, PlatformType platform, BigDecimal commission) {
    return modifyPointComm(cpId, bizId, funcCode, platform, commission,
        PointOperateType.COMMISSION);
  }

  private PointCommOperationResult modifyPointComm(Long cpId, String bizId, GradeCode grade,
      PlatformType platform, BigDecimal points, PointOperateType operateType) {
    checkNotNull(grade, "积分规则不能为空");
    CodeNameType gradeType = grade.getCodeName();
    checkNotNull(gradeType, "积分规则类型未指定");

    BasePointCommOperator operator = PointOperatorFactory.getOperator(gradeType);
    // 根据不同操作计算积分结果
    PointCommOperationResult ret = operator.doOperation(cpId, bizId, grade,
        platform, points, operateType);
    boolean saveRet = saveRet(bizId, grade, ret, operator, operateType.getRecordClazz());
    if (!saveRet) {
      throw new BizException(GlobalErrorCode.INTERNAL_ERROR, "内部错误, 请稍后再试");
    }
    return ret;
  }

  private PointCommOperationResult modifyPointComm(Long cpId, String bizId,
      String functionCode, PlatformType platform, BigDecimal points,
      PointOperateType operateType) {
    GradeCode grade = pointGradeService.loadByFunctionCode(functionCode);
    if (grade == null) {
      throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "规则代码无效");
    }
    return modifyPointComm(cpId, bizId, grade, platform, points, operateType);
  }

  /**
   * 保存或更新用户积分信息
   *
   * @param info 积分信息VO
   * @return 保存结果
   */
  @Override
  public boolean saveOrUpdate(BasePointCommTotal info) {
    boolean ret;
    if (info.getId() == null) {
      ret = saveTotal(info);
    } else {
      ret = updateTotal(info);
    }
    return ret;
  }

  @Override
  public PointTotal loadByCpId(Long cpId) {
    return pointTotalMapper.selectByCpId(cpId);
  }

  @Override
  public CommissionTotal loadCommByCpId(Long cpId) {
    return commissionTotalMapper.selectByCpId(cpId);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends BasePointCommTotal> T loadOrBuildInfo(Long cpId, Class<T> clazz) {
    T ret;
    if (clazz == PointTotal.class) {
      ret = (T) pointTotalMapper.selectByCpId(cpId);
    } else if (clazz == CommissionTotal.class) {
      ret = (T) commissionTotalMapper.selectByCpId(cpId);
    } else {
      throw new BizException(GlobalErrorCode.UNKNOWN, "积分形式不支持");
    }
    if (ret == null) {
      ret = BasePointCommTotal.emptyInfo(cpId, clazz);
    }
    return ret;
  }

  /**
   * 保存积分或德分 或其他乱七八糟分, 如果还有什么鬼分用抽象类去实现
   *
   * @param total 积分或德分
   * @return 保存结果
   */
  private boolean saveTotal(BasePointCommTotal total) {
    if (total instanceof PointTotal) {
      return pointTotalMapper.insert((PointTotal) total) > 0;
    } else if (total instanceof CommissionTotal) {
      return commissionTotalMapper.insert((CommissionTotal) total) > 0;
    } else {
      throw new BizException(GlobalErrorCode.POINT_NOT_SUPPORT);
    }
  }

  /**
   * 更新积分或德分 或其他乱七八糟分, 如果还有什么鬼分用抽象类去实现
   *
   * @param total 积分或德分
   * @return 更新结果
   */
  private boolean updateTotal(BasePointCommTotal total) {
    if (total instanceof PointTotal) {
      return pointTotalMapper.updateByPrimaryKeySelective((PointTotal) total) > 0;
    } else if (total instanceof CommissionTotal) {
      return commissionTotalMapper.updateByPrimaryKeySelective((CommissionTotal) total) > 0;
    } else {
      throw new BizException(GlobalErrorCode.POINT_NOT_SUPPORT);
    }
  }

  /**
   * 统一保存积分修改结果
   *
   * @param operationResult 积分计算结果
   * @param operator 规则实现类
   * @param clazz 积分记录class类型
   * @return 保存结果
   */
  @Transactional(rollbackFor = Exception.class)
  boolean saveRet(String bizId, GradeCode grade, PointCommOperationResult operationResult,
      BasePointCommOperator operator,
      Class<? extends BasePointCommRecord> clazz) {

    // 保存积分记录
    boolean ret = operator.saveBackRecord(bizId, grade, operationResult, clazz);

    // 更新或保存用户积分信息
    BasePointCommTotal infoAfter = operationResult.getInfoAfter();
    ret = ret && saveOrUpdate(infoAfter);
    // 保存积分记录积分记录
    return ret;
  }

}
