package com.hds.xquark.service.point.impl;

import com.hds.xquark.dal.model.CommissionTotal;
import com.hds.xquark.dal.type.PlatformType;
import com.hds.xquark.dal.type.Trancd;
import com.hds.xquark.service.point.helper.PointCommCalHelper;
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

  private final Long cpId = 300030L;
  private final BigDecimal modifyPoints = BigDecimal.valueOf(1);

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
        modifyPoints, trancd, null);

    pointCommService.modifyCommission(
        300028L,
        bizId,
        "1004",
        PlatformType.H,
        modifyPoints, trancd, null);

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
        modifyPoints, Trancd.ROYA, null);

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
        "2001",
        PlatformType.E,
        modifyPoints, trancd, null);

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
  public void testConsume() {

    CommissionTotal totalBefore = pointCommService.loadCommByCpId(cpId);

    pointCommService.modifyPoint(
        cpId,
        getBizId(),
        "2002",
        PlatformType.H,
        modifyPoints, Trancd.ROYA, null);

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
        300028L,
        bizId,
        "2002",
        PlatformType.E,
        modifyPoints, trancd, null);

    pointCommService.modifyCommission(
        300028L,
        bizId,
        "2004",
        PlatformType.H,
        modifyPoints, trancd, null);

    CommissionTotal rollbackAfter = pointCommService.loadCommByCpId(cpId);
    Assert.assertEquals(totalBefore.getTotal(), rollbackAfter.getTotal());
  }

  @Test
  public void testFreezeRelease() {
    pointCommService.releaseCommission(null);
  }

  private String getBizId() {
    return String.valueOf(new Date().getTime());
  }

  @Test
  public void grantByProcedure() {
    pointCommService.grantCommissionWithProcedure(cpId, PlatformType.E, BigDecimal.valueOf(200),
        Trancd.MIGRATE_C);
  }

}
