package com.hds.xquark.service.point.impl;

import com.hds.xquark.config.PointDalConfig;
import com.hds.xquark.config.PointServiceConfig;
import com.hds.xquark.dal.model.PointTotal;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.service.point.PointCommOperationResult;
import com.hds.xquark.service.point.PointCommService;
import com.hds.xquark.service.point.helper.PointCommCalHelper;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.Assert;
import org.junit.Before;
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

  private final BigDecimal modifyPoints = BigDecimal.valueOf(1);

  @Autowired
  PointCommService pointCommService;

  @Before
  public void init() {
    PointTotal total = pointCommService.loadByCpId(cpId);
    // 初始化数据
    if (total == null) {
      pointCommService.modifyPoint(
          300028L,
          getBizId(),
          "1001",
          PlatformType.E,
          modifyPoints, Trancd.ACHA);
    }
  }

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
    PlatformType platform = PlatformType.E;

    pointCommService.modifyPoint(
        cpId,
        getBizId(),
        "1003",
        platform,
        modifyPoints, Trancd.ROYA);

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
        modifyPoints, trancd);

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
        modifyPoints, Trancd.ROYA);

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
        modifyPoints, Trancd.ROYA);

    pointCommService.releasePoints();
    PointTotal totalAfter = pointCommService.loadByCpId(cpId);
    Assert
        .assertEquals(totalBefore.getUsableEcomm(),
            totalAfter.getUsableEcomm().subtract(modifyPoints));
  }

  private String getBizId() {
    return String.valueOf(new Date().getTime());
  }
}