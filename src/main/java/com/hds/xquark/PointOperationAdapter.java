package com.hds.xquark;

import com.hds.xquark.config.PointDalConfig;
import com.hds.xquark.config.PointServiceConfig;
import com.hds.xquark.dal.util.SpringContextUtil;
import com.hds.xquark.service.point.PointCommService;
import com.hds.xquark.service.point.impl.PointCommServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PointOperationAdapter {

  private static final SpringContextUtil CONTEXT_UTIL;

  static {
    ApplicationContext context = new AnnotationConfigApplicationContext(PointServiceConfig.class,
        PointDalConfig.class);
    CONTEXT_UTIL = new SpringContextUtil();
    CONTEXT_UTIL.setApplicationContext(context);
  }

  public static PointCommService getPointService() {
    return SpringContextUtil.getBean(PointCommServiceImpl.class);
  }

  public static void main(String[] args) {
    PointCommService commService = getPointService();
    System.out.println(commService.loadByCpId(300001L));
  }

}
