package com.hds.xquark.service.point;

import com.hds.xquark.dal.model.BasePointCommAsst;
import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.dal.model.CommissionRecord;
import com.hds.xquark.dal.model.CommissionTotal;
import com.hds.xquark.dal.model.CustomerWithdrawal;
import com.hds.xquark.dal.model.PointTotal;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.TotalAuditType;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.dal.vo.CommissionWithdrawVO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangxinhua on 2018/5/18. DESC: 积分信息service
 */
public interface PointCommService {

  PointCommOperationResult modifyPoint(Long cpId, String bizId,
      Integer categoryId, Integer status, PlatformType platform, BigDecimal points, Trancd trancd,
      TotalAuditType auditType);

  PointCommOperationResult modifyCommission(Long cpId, String bizId,
      Integer categoryId, Integer status, PlatformType platform, BigDecimal commission,
      Trancd trancd, TotalAuditType auditType);

  /**
   * 根据积分规则处理积分
   *
   * @param bizId 业务id
   * @param funcCode 规则代码
   * @param platform 所属平台
   * @param points 改动积分数，若规则本身设置了静态积分则不生效
   * @param trancd
   * @param auditType
   * @return {@link PointCommOperationResult} 积分处理结果
   */
  PointCommOperationResult modifyPoint(Long cpId, String bizId,
      String funcCode, PlatformType platform, BigDecimal points,
      Trancd trancd, TotalAuditType auditType);

  /**
   * 根据德分规则处理积分
   *
   * @param bizId 业务id
   * @param funcCode 规则代码
   * @param platform 所属平台
   * @param commission 改动积分数，若规则本身设置了静态积分则不生效
   * @param trancd
   * @param auditType
   * @return {@link PointCommOperationResult} 积分处理结果
   */
  PointCommOperationResult modifyCommission(Long cpId, String bizId,
      String funcCode, PlatformType platform, BigDecimal commission,
      Trancd trancd, TotalAuditType auditType);

  /**
   * 更新或新增用户积分信息
   *
   * @param info 积分对象
   * @param auditType
   * @return true or false
   */
  boolean saveOrUpdate(BasePointCommTotal info,
      TotalAuditType auditType);

  PointTotal loadByCpId(Long cpId);

  CommissionTotal loadCommByCpId(Long cpId);

  /**
   * 获取用户最后更新的积分记录, 若不存在则返回一个空数据
   *
   * @param cpId 用户id
   * @param clazz 积分/德分类型
   * @return 积分信息, 不能为空
   */
  @SuppressWarnings("unchecked")
  <T extends BasePointCommTotal> T loadOrBuildInfo(Long cpId, Class<T> clazz);

  <T extends BasePointCommTotal> BigDecimal sumTotal(String gradeCode, Long cpId, Trancd trancd,
      Class<T> clazz);

  boolean saveTotal(BasePointCommTotal total,
      TotalAuditType auditType);

  boolean updateTotal(BasePointCommTotal total,
      TotalAuditType auditType);

  Map<String, Object> listPointRecords(Long cpId, Integer source, Integer offset, Integer size);

  Map<String, Object> listCommissionRecords(Long cpId, Integer source, Integer offset,
      Integer size);

  int releaseCommission(TotalAuditType auditType);

  int releasePoints(TotalAuditType auditType);

  /**
   * 调用存储过程增加积分
   *
   * @param cpId cpId
   * @param platform 平台
   * @param val 发放值
   * @param trancd tranCode 合法值 REWROD_C, DEPOSIT_C, MIGRATE_C
   */
  void grantPointWithProcedure(Long cpId, PlatformType platform, BigDecimal val, Trancd trancd);

  void grantCommissionWithProcedure(Long cpId, PlatformType platform, BigDecimal val,
      Trancd trancd);

  List<CommissionRecord> listRecordByTime(Date start, Date end, String grade);

  int translateCommSuspendingToWithdrawLastMonth(Date from);

  int translateCommSuspendingToWithdraw(Date start, Date end);

  List<CommissionWithdrawVO> listWithdrawVO(Integer orderMonth, PlatformType source);

  List<CommissionWithdrawVO> listZHWithdrawVO(Integer orderMonth, PlatformType source);

  List<CommissionWithdrawVO> listNonZHWithdrawVO(Integer orderMonth, PlatformType source);

  List<CustomerWithdrawal> listWithDraw(Long cpId, Integer month, Integer source);

  boolean updateCustomerWithdrawByMonth(CustomerWithdrawal withdrawal);

  boolean updateCustomerWithdrawById(CustomerWithdrawal withdrawal);

  boolean isCpIdWithdrawed(Long cpId, Integer month, Integer source);

  boolean isOrderMonthProcessed(Integer month);

  List<String> listWithdrawTopMonth(int month);

  <T extends BasePointCommAsst> List<T> listAsst(String bizId, Long cpId, Trancd trancd,
      Class<T> clazz);
}
