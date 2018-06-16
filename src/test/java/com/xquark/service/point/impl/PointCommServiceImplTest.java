package com.xquark.service.point.impl;

import com.xquark.config.PointDalConfig;
import com.xquark.config.PointServiceConfig;
import com.xquark.dal.model.PointTotal;
import com.xquark.dal.type.PlatformType;
import com.xquark.service.point.PointCommOperationResult;
import com.xquark.service.point.PointCommService;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * created by
 *
 * @author wangxinhua at 18-6-16 上午11:57
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PointServiceConfig.class, PointDalConfig.class})
public class PointCommServiceImplTest {

  @Autowired
  PointCommService pointCommService;

  @Test
  public void modifyPoint() {
    Long cpId = 300028L;
    BigDecimal modifyPoints = BigDecimal.valueOf(2000);

    PointTotal totalBefore = pointCommService.loadByCpId(cpId);

    PointCommOperationResult ret = pointCommService.modifyPoint(
        300028L,
        "1234",
        "1001",
        PlatformType.H,
        modifyPoints);

    PointTotal totalAfter = pointCommService.loadByCpId(cpId);
    Assert.assertEquals(totalBefore.getUsableHds(), totalAfter.getUsableHds().subtract(modifyPoints));
  }

  @Test
  public void modifyCommission() {
  }
}