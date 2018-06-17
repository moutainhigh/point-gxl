package com.hds.xquark.service.point.impl;

import com.hds.xquark.config.PointDalConfig;
import com.hds.xquark.config.PointServiceConfig;
import com.hds.xquark.dal.model.CommissionTotal;
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
 * @author wangxinhua at 18-6-17 下午2:49
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PointServiceConfig.class, PointDalConfig.class})
@Transactional
public class CommissionOperationTest {

  private final Long cpId = 300028L;
  private final BigDecimal modifyPoints = BigDecimal.valueOf(50000);
  @Autowired
  private PointCommService pointCommService;

  @Test
  public void testFreezeGrantRollBack() {
    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);
    String bizId = String.valueOf(new Date().getTime());

    Trancd trancd = Trancd.VF3;

    pointCommService.modifyCommission(
        300028L,
        bizId,
        "1003",
        PlatformType.H,
        modifyPoints, trancd);

    pointCommService.modifyCommission(
        300028L,
        bizId,
        "1004",
        PlatformType.H,
        modifyPoints, trancd);

    CommissionTotal rollbackAfter = pointCommService.loadCommByCpId(cpId);
    Assert.assertEquals(totalBefore.getFreezedHds(), rollbackAfter.getFreezedHds());
  }

  @Test
  public void testGrant() {
    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);
    Trancd trancd = Trancd.PRBA;

    pointCommService.modifyCommission(
        300028L,
        getBizId(),
        "2001",
        PlatformType.V,
        modifyPoints, trancd);

    CommissionTotal totalAfter = pointCommService.loadCommByCpId(cpId);
    // 未初始化
    if (totalBefore == null) {
      Assert.assertEquals(totalAfter.getUsableViviLife(), modifyPoints);
    } else {
      Assert
          .assertEquals(totalBefore.getUsableViviLife(),
              totalAfter.getUsableViviLife().subtract(modifyPoints));
    }
  }

  @Test
  public void testConsumeRollBack() {
    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);
    String bizId = getBizId();

    Trancd trancd = Trancd.ACHA;

    pointCommService.modifyPoint(
        300028L,
        bizId,
        "2002",
        PlatformType.E,
        modifyPoints, trancd);

    pointCommService.modifyPoint(
        300028L,
        bizId,
        "2004",
        PlatformType.H,
        modifyPoints, trancd);

    CommissionTotal rollbackAfter = pointCommService.loadCommByCpId(cpId);
    Assert.assertEquals(totalBefore.getTotal(), rollbackAfter.getTotal());
  }

  private String getBizId() {
    return String.valueOf(new Date().getTime());
  }

}
