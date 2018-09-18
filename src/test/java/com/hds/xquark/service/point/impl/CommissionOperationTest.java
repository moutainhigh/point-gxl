package com.hds.xquark.service.point.impl;

import static com.hds.xquark.dal.type.TotalAuditType.API;

import com.hds.xquark.dal.constrant.GradeCodeConstrants;
import com.hds.xquark.dal.model.CommissionTotal;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.TotalAuditType;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.service.point.helper.PointCommCalHelper;
import com.hds.xquark.utils.DateUtils;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * created by
 *
 * @author wangxinhua at 18-6-17 下午2:49
 */
@Transactional
public class CommissionOperationTest extends BaseOperationTest {

  private final Long cpId = 1000080L;
  private final BigDecimal modifyPoints = BigDecimal.valueOf(1);

  private final TotalAuditType auditType = API;

  @Test
  public void testFreezeGrantRollBack() {
    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);
    String bizId = String.valueOf(new Date().getTime());

    Trancd trancd = Trancd.VF3;

    pointCommService.modifyCommission(
        cpId,
        bizId,
        "1003",
        PlatformType.H,
        modifyPoints, trancd, auditType);

    pointCommService.modifyCommission(
        cpId,
        bizId,
        "1004",
        PlatformType.H,
        modifyPoints, trancd, auditType);

    CommissionTotal rollbackAfter = pointCommService.loadCommByCpId(cpId);
    Assert.assertEquals(totalBefore.getFreezedHds(), rollbackAfter.getFreezedHds());
  }

  @Test
  public void testFreezeGrant() {
    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);
    PlatformType platform = PlatformType.E;

    pointCommService.modifyCommission(
        cpId,
        getBizId(),
        "2003",
        platform,
        modifyPoints, Trancd.ROYA, auditType);

    CommissionTotal totalAfter = pointCommService.loadCommByCpId(cpId);

    Assert.assertEquals(PointCommCalHelper.getFreezed(totalBefore, platform),
        PointCommCalHelper.getFreezed(totalAfter, platform).subtract(modifyPoints));
  }

  @Test
  public void testGrant() {
    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);
    Trancd trancd = Trancd.PRBA;

    pointCommService.modifyCommission(
        cpId,
        getBizId(),
        GradeCodeConstrants.GRANT_COMMISSION_CODE,
        PlatformType.E,
        modifyPoints, trancd, auditType);

    CommissionTotal totalAfter = pointCommService.loadCommByCpId(cpId);
    // 未初始化
    if (totalBefore == null) {
      Assert.assertEquals(totalAfter.getUsableEcomm(), modifyPoints);
    } else {
      Assert
          .assertEquals(totalBefore.getUsableEcomm(),
              totalAfter.getUsableEcomm().subtract(modifyPoints));
    }
  }

  @Test
  public void testWithdraw() {
    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);
    pointCommService
        .modifyCommission(cpId, "withdraw", GradeCodeConstrants.WITH_DRAW_COMMISSION_CODE,
            PlatformType.V,
            modifyPoints, Trancd.WITHDRAW_C, API);
    CommissionTotal totalAfter = pointCommService.loadCommByCpId(cpId);
    Assert
        .assertEquals(totalBefore.getTotal(), totalAfter.getTotal().add(modifyPoints));
  }

  @Test
  public void testConsume() {

    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);

    pointCommService.modifyCommission(
        cpId,
        getBizId(),
        GradeCodeConstrants.CONSUME_COMMISSION_CODE,
        PlatformType.H,
        modifyPoints, Trancd.ROYA, auditType);

    CommissionTotal totalAfter = pointCommService.loadCommByCpId(cpId);
    Assert
        .assertEquals(totalBefore.getTotal(), totalAfter.getTotal().add(modifyPoints));
  }

  @Test
  public void testConsumeRollBack() {
    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);
    String bizId = getBizId();

    Trancd trancd = Trancd.ACHA;

    pointCommService.modifyCommission(
        cpId,
        bizId,
        "2002",
        PlatformType.E,
        modifyPoints, trancd, auditType);

    pointCommService.modifyCommission(
        cpId,
        bizId,
        "2004",
        PlatformType.H,
        modifyPoints, trancd, auditType);

    CommissionTotal rollbackAfter = pointCommService.loadCommByCpId(cpId);
    Assert.assertEquals(totalBefore.getTotal(), rollbackAfter.getTotal());
  }

  @Test
  public void testFreezeRelease() {
    pointCommService.releaseCommission(auditType);
  }

  private String getBizId() {
    return String.valueOf(new Date().getTime());
  }

  @Test
  public void grantByProcedure() {
    pointCommService.grantCommissionWithProcedure(cpId, PlatformType.E, BigDecimal.valueOf(200),
        Trancd.DEPOSIT_C);
  }

  @Test
  public void testTransformWithdraw() {
    pointCommService.translateCommSuspendingToWithdraw(new Date(), DateUtils.addDay(new Date(), 1));
  }

  @Test
  public void testListWithdraw() {
    System.out.println(pointCommService.listWithdrawVO(201808, PlatformType.H));
  }

  @Test
  public void testSumTotal() {
    System.out.println(pointCommService
        .sumTotal(GradeCodeConstrants.GRANT_COMMISSION_CODE, cpId, , CommissionTotal.class, ));
  }

  @Test
  public void testListVO() {
    Assert.assertNotNull(pointCommService.listCommissionRecords(cpId, null, null, null));
  }

}
