package com.hds.xquark.service.point.impl;

import com.hds.xquark.dal.constrant.GradeCodeConstrants;
import com.hds.xquark.dal.constrant.PointConstrants;
import com.hds.xquark.dal.model.CommissionTotal;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.TotalAuditType;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.service.point.helper.PointCommCalHelper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static com.hds.xquark.dal.type.TotalAuditType.API;

/**
 * created by
 *
 * @author wangxinhua at 18-6-17 下午2:49
 */
@Transactional
public class CommissionOperationTest extends BaseOperationTest {

  private final Long cpId = 3111111L;
  private final BigDecimal modifyPoints = BigDecimal.valueOf(50);

  private final TotalAuditType auditType = API;

  @Test
  public void testFreezeGrantRollBack() {
    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);
    String bizId = String.valueOf(new Date().getTime());

    Trancd trancd = Trancd.VF3;

    pointCommService.modifyCommission(
        cpId, bizId, "1003", PlatformType.H, modifyPoints, trancd, auditType);

    pointCommService.modifyCommission(
        cpId, bizId, "1004", PlatformType.H, modifyPoints, trancd, auditType);

    CommissionTotal rollbackAfter = pointCommService.loadCommByCpId(cpId);
    Assert.assertEquals(totalBefore.getFreezedHds(), rollbackAfter.getFreezedHds());
  }

  @Test
  public void testFreezeGrant() {
    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);
    PlatformType platform = PlatformType.E;

    pointCommService.modifyCommission(
        cpId, getBizId(), "2003", platform, modifyPoints, Trancd.ROYA, auditType);

    CommissionTotal totalAfter = pointCommService.loadCommByCpId(cpId);

    Assert.assertEquals(
        PointCommCalHelper.getFreezed(totalBefore, platform),
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
        modifyPoints,
        trancd,
        auditType);

    CommissionTotal totalAfter = pointCommService.loadCommByCpId(cpId);
    // 未初始化
    if (totalBefore == null) {
      Assert.assertEquals(totalAfter.getUsableEcomm(), modifyPoints);
    } else {
      Assert.assertEquals(
          totalBefore.getUsableEcomm(), totalAfter.getUsableEcomm().subtract(modifyPoints));
    }
  }

  @Test
  public void testWithdraw() {
    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);
    pointCommService.modifyCommission(
        cpId,
        "withdraw",
        GradeCodeConstrants.WITH_DRAW_COMMISSION_CODE,
        PlatformType.V,
        modifyPoints,
        Trancd.WITHDRAW_C,
        API);
    CommissionTotal totalAfter = pointCommService.loadCommByCpId(cpId);
    Assert.assertEquals(totalBefore.getTotal(), totalAfter.getTotal().add(modifyPoints));
  }

  @Test
  public void testConsume() {

    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);

    pointCommService.modifyCommission(
        cpId,
        getBizId(),
        GradeCodeConstrants.CONSUME_COMMISSION_CODE,
        PlatformType.H,
        modifyPoints,
        Trancd.ROYA,
        auditType);

    CommissionTotal totalAfter = pointCommService.loadCommByCpId(cpId);
    Assert.assertEquals(totalBefore.getTotal(), totalAfter.getTotal().add(modifyPoints));
  }

  @Test
  public void testConsumeRollBack() {
    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);
    String bizId = getBizId();

    Trancd trancd = Trancd.ACHA;

    pointCommService.modifyCommission(
        cpId,
        bizId,
        GradeCodeConstrants.CONSUME_COMMISSION_CODE,
        PlatformType.H,
        modifyPoints,
        trancd,
        auditType);

    pointCommService.modifyCommission(
        cpId,
        bizId,
        GradeCodeConstrants.CANCEL_COMMISSION_CODE,
        PlatformType.H,
        modifyPoints,
        trancd,
        auditType);

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
    pointCommService.grantCommissionWithProcedure(
        cpId, PlatformType.E, BigDecimal.valueOf(200), 2, Trancd.DEPOSIT_C);
  }

  @Test
  public void testTransformWithdraw() {
    pointCommService.translateCommSuspendingToWithdraw(new Date(), PlatformType.V);
  }

  @Test
  public void testListWithdraw() {
    System.out.println(pointCommService.listWithdrawVO(201808, PlatformType.H));
  }

  @Test
  public void testSumTotal() {
    System.out.println(
        pointCommService.sumTotal(
            GradeCodeConstrants.GRANT_COMMISSION_CODE,
            cpId,
            Trancd.REWARD_C,
            CommissionTotal.class));
  }

  @Test
  public void testListVO() {
    Assert.assertNotNull(pointCommService.listCommissionRecords(cpId, null, null, null));
  }

  @Test
  public void testPointNew() {
    //    getInitialize().getPointService().loadByCpId(cpId);
    //    PointCommOperationResult<PointTotal, PointRecord> modify =
    // getInitialize().getPointServiceNew()
    //        .modify(cpId, getBizId(), FunctionCodeType.getPacketSend(), PlatformType.E,
    //            BigDecimal.valueOf(10));
    //    System.out.println(modify);
    //    PointTotal pointTotal = getInitialize().getPointServiceNew().loadTotal(cpId);

    CommissionTotal forUpdate = getInitialize().getCommissionServiceApi().initTotal(cpId);
    forUpdate.setCpId(cpId);
    forUpdate.setUsableCommHds(BigDecimal.valueOf(20000));
    getInitialize().getCommissionServiceApi().updateByCpId(forUpdate);
    System.out.println(forUpdate);
  }

  @Test
  public void testTransform() {
    getCommissionServiceApi()
        .transform(
            cpId,
            BigDecimal.valueOf(10),
            PlatformType.E,
            PointConstrants.COMM_TO_POINT_RATE,
            getPointServiceApi());
  }
}
