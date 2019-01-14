package com.hds.xquark.service.point.impl;

import com.hds.xquark.PointContextInitialize;
import com.hds.xquark.dal.model.BasePointCommRecord;
import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.service.point.PointCommService;
import com.hds.xquark.service.point.TokenService;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.junit.Before;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author wangxinhua on 2018/7/27. DESC:
 */
public abstract class BaseOperationTest<T extends TokenService> {

  PointCommService pointCommService;
  private PointContextInitialize initialize;

  @Before
  public void init() {
    DataSource dataSource = new PooledDataSource(
        "com.mysql.jdbc.Driver",
        "jdbc:mysql://rm-uf6o788t14snm5j623o.mysql.rds.aliyuncs.com:1666/hvmall?autoCommit=true&useUnicode=true&autoReconnect=true&characterEncoding=UTF-8",
        "byyroot",
        "v7&#5efr&777");
    PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
    this.initialize = new PointContextInitialize(dataSource, transactionManager);
    pointCommService = this.initialize.getPointService();
  }

  protected abstract T getTokenService();

  public PointContextInitialize getInitialize() {
    return initialize;
  }

  public void testTransform() {

  }
}
