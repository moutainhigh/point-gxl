package com.hds.xquark.service.point.impl;

import com.hds.xquark.dal.constrant.GradeCodeConstrants;
import com.hds.xquark.dal.model.BasePointCommAsst;
import com.hds.xquark.dal.model.PointSuspendingAsst;
import com.hds.xquark.dal.model.PointTotal;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.TotalAuditType;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.service.point.PointCommOperationResult;
import com.hds.xquark.service.point.helper.PointCommCalHelper;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * created by
 *
 * @author wangxinhua at 18-6-16 上午11:57
 */
@Transactional
public class PointOperationTest extends BaseOperationTest {

  private final Long cpId = 3000000L;

  private final BigDecimal modifyPoints = BigDecimal.valueOf(50);

  private final TotalAuditType auditType = TotalAuditType.DTS;

  private final static Logger LOGGER = Logger.getLogger(PointOperationTest.class);

  @Test
  public void testBase() {
    PointTotal pointTotal = pointCommService.loadByCpId(cpId);
    System.out.println(pointTotal);
  }

  @Test
  public void testRepeatConsume() {
    // TODO assertException
//    PointTotal totalBefore = pointCommService.loadByCpId(cpId);
//    String bizId = getBizId();
//
//    pointCommService.modifyPoint(
//        cpId,
//        bizId,
//        "1002",
//        PlatformType.H,
//        modifyPoints, Trancd.ROYA, auditType);
//
//    pointCommService.modifyPoint(
//        cpId,
//        bizId,
//        "1002",
//        PlatformType.H,
//        modifyPoints, Trancd.ROYA, auditType);
//
//    PointTotal totalAfter = pointCommService.loadByCpId(cpId);
//    Assert
//        .assertEquals(totalBefore.getTotal(), totalAfter.getTotal().add(modifyPoints));
  }

  @Test
  public void testConsumeRollBack() {
    PointTotal totalBefore = pointCommService.loadByCpId(cpId);
    String bizId = getBizId();

    Trancd trancd = Trancd.ACHA;

    pointCommService.modifyPoint(
        cpId,
        bizId,
        "1002",
        PlatformType.H,
        modifyPoints, trancd, auditType);

    pointCommService.modifyPoint(
        cpId,
        bizId,
        "1004",
        PlatformType.H,
        modifyPoints, trancd, auditType);

    PointTotal rollbackAfter = pointCommService.loadByCpId(cpId);

    List<? extends BasePointCommAsst> assts = pointCommService.listAsst(bizId, cpId, trancd,
        PointSuspendingAsst.class);
    Assert.assertEquals(totalBefore.getTotal(), rollbackAfter.getTotal());
    Assert.assertTrue(CollectionUtils.isNotEmpty(assts));

    // 50.0.equals(50.00) is not true
    Assert.assertTrue(assts.get(0).getCurrent().abs().compareTo(modifyPoints) == 0);
    Assert.assertTrue(assts.get(1).getCurrent().abs().compareTo(modifyPoints) == 0);
  }

  @Test
  public void testFreezeGrantRollBack() {
    PointTotal totalBefore = pointCommService.loadByCpId(cpId);
    String bizId = String.valueOf(new Date().getTime());

    Trancd trancd = Trancd.VF3;

    pointCommService.modifyPoint(
        cpId,
        bizId,
        100,
        1,
        PlatformType.H,
        modifyPoints, trancd, auditType);

    pointCommService.modifyPoint(
        cpId,
        bizId,
        "1004",
        PlatformType.H,
        modifyPoints, trancd, auditType);

    PointTotal rollbackAfter = pointCommService.loadByCpId(cpId);
    Assert.assertEquals(totalBefore.getFreezedHds(), rollbackAfter.getFreezedHds());
  }


  @Test
  public void testGrantRollBack() {
    PointTotal totalBefore = pointCommService.loadByCpId(cpId);
    String bizId = String.valueOf(new Date().getTime());

    pointCommService.modifyPoint(
        cpId,
        bizId,
        GradeCodeConstrants.GRANT_POINT_CODE,
        PlatformType.H,
        modifyPoints, Trancd.PRBA, auditType);

//    Assert.assertEquals(totalBefore.getFreezedHds(),
//        totalAfter.getFreezedHds().subtract(modifyPoints));

    pointCommService.modifyPoint(
        cpId,
        bizId,
        GradeCodeConstrants.CANCEL_POINT_CODE,
        PlatformType.H,
        modifyPoints, Trancd.PRBA, auditType);

    PointTotal rollbackAfter = pointCommService.loadByCpId(cpId);
    Assert.assertEquals(totalBefore.getUsableHds(), rollbackAfter.getUsableHds());

  }

  @Test
  public void testFreezeGrant() {
    PointTotal totalBefore = pointCommService.loadByCpId(cpId);
    PlatformType platform = PlatformType.E;

    pointCommService.modifyPoint(
        cpId,
        getBizId(),
        "1003",
        platform,
        modifyPoints, Trancd.ROYA, auditType);

    PointTotal totalAfter = pointCommService.loadByCpId(cpId);

    Assert.assertEquals(PointCommCalHelper.getFreezed(totalBefore, platform),
        PointCommCalHelper.getFreezed(totalAfter, platform).subtract(modifyPoints));
  }

  @Test
  public void testGrant() {
    PointTotal totalBefore = pointCommService.loadByCpId(cpId);
    Trancd trancd = Trancd.PRBA;

    pointCommService.modifyPoint(
        cpId,
        getBizId(),
        "1001",
        PlatformType.V,
        modifyPoints, trancd, auditType);

    PointTotal totalAfter = pointCommService.loadByCpId(cpId);
    Assert
        .assertEquals(totalBefore.getUsableViviLife(),
            totalAfter.getUsableViviLife().subtract(modifyPoints));
  }

  @Test
  public void testConsume() {

    PointTotal totalBefore = pointCommService.loadByCpId(cpId);

    PointCommOperationResult pointCommOperationResult = pointCommService.modifyPoint(
        cpId,
        getBizId(),
        "1002",
        PlatformType.H,
        BigDecimal.valueOf(50), Trancd.ROYA, auditType);

    PointTotal totalAfter = pointCommService.loadByCpId(cpId);
    Assert
        .assertEquals(totalBefore.getTotal(), totalAfter.getTotal().add(modifyPoints));
  }

  @Test
  public void testFreezeRelease() {
    PointTotal totalBefore = pointCommService.loadByCpId(cpId);
    PlatformType platform = PlatformType.E;

    pointCommService.modifyPoint(
        cpId,
        getBizId(),
        "1003",
        platform,
        modifyPoints, Trancd.ROYA, auditType);

    pointCommService.releasePoints(auditType);
    PointTotal totalAfter = pointCommService.loadByCpId(cpId);
//    Assert
//        .assertEquals(totalBefore.getUsableEcomm(),
//            totalAfter.getUsableEcomm().subtract(modifyPoints));
  }

  @Test
  public void testLog() {
    LOGGER.info(MessageFormat.format("---- test log {0} ---", "haha"),
        new RuntimeException("run time"));
  }

  @Test
  public void grantByProcedure() {
    pointCommService
        .grantPointWithProcedure(cpId, PlatformType.E, BigDecimal.valueOf(200), Trancd.REWARD_P);
  }

  @Test
  public void testListRecords() {
    Assert.assertNotNull(pointCommService.listPointRecords(cpId, null, null, null));
  }

  @Test
  public void testRollbackAsst() {
    testConsumeRollBack();

  }

  private String getBizId() {
    return String.valueOf(new Date().getTime());
  }

}