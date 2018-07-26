package com.hds.xquark;

import com.hds.xquark.config.PointDalConfig;
import com.hds.xquark.config.PointServiceConfig;
import com.hds.xquark.service.point.PointCommService;
import com.hds.xquark.service.point.impl.PointCommServiceImpl;
import javax.sql.DataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 容器初始化类, 传入数据源供外部调用
 */
public class PointContextInitialize {

  private final PointCommService pointCommService;

  public PointContextInitialize(DataSource dataSource,
      PlatformTransactionManager transactionManager) {
    // 将外部数据源初始化到父容器
    StaticApplicationContext dsContext = new StaticApplicationContext();
    dsContext.refresh();
    dsContext.getBeanFactory().registerSingleton("dataSource", dataSource);
    dsContext.getBeanFactory().registerSingleton("transactionManager", transactionManager);

    // 将真实的容器作为子容器初始化
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.setParent(dsContext);
    context.register(PointDalConfig.class);
    context.register(PointServiceConfig.class);
    context.refresh();

    this.pointCommService = context.getBean(PointCommServiceImpl.class);
  }

  public PointCommService getPointService() {
    return this.pointCommService;
  }

}
