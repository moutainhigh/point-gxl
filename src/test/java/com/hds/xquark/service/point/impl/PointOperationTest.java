package com.hds.xquark.service.point.impl;

import com.hds.xquark.config.PointDalConfig;
import com.hds.xquark.config.PointServiceConfig;
import com.hds.xquark.dal.model.PointTotal;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.service.point.PointCommService;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * created by
 *
 * @author wangxinhua at 18-6-16 上午11:57
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PointServiceConfig.class, PointDalConfig.class})
@Transactional
public class PointOperationTest {

  private final Long cpId = 300028L;

  private final BigDecimal modifyPoints = BigDecimal.valueOf(50000);

  @Autowired
  PointCommService pointCommService;

  @Test
  public void testConsumeRollBack() {
    PointTotal totalBefore = pointCommService.loadByCpId(cpId);
    String bizId = getBizId();

    Trancd trancd = Trancd.ACHA;

    pointCommService.modifyPoint(
        300028L,
        bizId,
        "1002",
        PlatformType.E,
        modifyPoints, trancd);

    pointCommService.modifyPoint(
        300028L,
        bizId,
        "1004",
        PlatformType.H,
        modifyPoints, trancd);

    PointTotal rollbackAfter = pointCommService.loadByCpId(cpId);
    Assert.assertEquals(totalBefore.getTotal(), rollbackAfter.getTotal());
  }

  @Test
  public void testFreezeGrantRollBack() {
    PointTotal totalBefore = pointCommService.loadByCpId(cpId);
    String bizId = String.valueOf(new Date().getTime());

    Trancd trancd = Trancd.VF3;

    pointCommService.modifyPoint(
        300028L,
        bizId,
        100,
        1,
        PlatformType.H,
        modifyPoints, trancd);

    pointCommService.modifyPoint(
        300028L,
        bizId,
        "1004",
        PlatformType.H,
        modifyPoints, trancd);

    PointTotal rollbackAfter = pointCommService.loadByCpId(cpId);
    Assert.assertEquals(totalBefore.getFreezedHds(), rollbackAfter.getFreezedHds());
  }


  @Test
  public void testGrantRollBack() {
    PointTotal totalBefore = pointCommService.loadByCpId(cpId);
    String bizId = String.valueOf(new Date().getTime());

    pointCommService.modifyCommission(
        300028L,
        bizId,
        "1001",
        PlatformType.H,
        modifyPoints, Trancd.PRBA);

//    Assert.assertEquals(totalBefore.getFreezedHds(),
//        totalAfter.getFreezedHds().subtract(modifyPoints));

    pointCommService.modifyCommission(
        300028L,
        bizId,
        "1004",
        PlatformType.H,
        modifyPoints, Trancd.PRBA);

    PointTotal rollbackAfter = pointCommService.loadByCpId(cpId);
    Assert.assertEquals(totalBefore.getUsableHds(), rollbackAfter.getUsableHds());

  }

  @Test
  public void testFreezeGrant() {
    PointTotal totalBefore = pointCommService.loadByCpId(cpId);

    pointCommService.modifyPoint(
        300028L,
        "12345",
        "1003",
        PlatformType.H,
        modifyPoints, Trancd.ROYA);

    PointTotal totalAfter = pointCommService.loadByCpId(cpId);

    Assert.assertEquals(totalBefore.getFreezedHds(),
        totalAfter.getFreezedHds().subtract(modifyPoints));
  }

  @Test
  public void testGrant() {
    PointTotal totalBefore = pointCommService.loadByCpId(cpId);
    Trancd trancd = Trancd.PRBA;

    pointCommService.modifyPoint(
        300028L,
        getBizId(),
        "1001",
        PlatformType.V,
        modifyPoints, trancd);

    PointTotal totalAfter = pointCommService.loadByCpId(cpId);
    Assert
        .assertEquals(totalBefore.getUsableViviLife(),
            totalAfter.getUsableViviLife().subtract(modifyPoints));
  }

  @Test
  public void testConsume() {

    PointTotal totalBefore = pointCommService.loadByCpId(cpId);

    pointCommService.modifyPoint(
        300028L,
        "1234",
        "1001",
        PlatformType.H,
        modifyPoints, Trancd.ROYA);

    PointTotal totalAfter = pointCommService.loadByCpId(cpId);
    Assert
        .assertEquals(totalBefore.getUsableHds(), totalAfter.getUsableHds().subtract(modifyPoints));
  }

  @Test
  public void modifyCommission() {
  }

  private String getBizId() {
    return String.valueOf(new Date().getTime());
  }
}