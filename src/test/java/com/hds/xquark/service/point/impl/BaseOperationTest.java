package com.hds.xquark.service.point.impl;

import com.hds.xquark.PointContextInitialize;
import com.hds.xquark.service.point.PointCommService;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.junit.Before;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author wangxinhua on 2018/7/27. DESC:
 */
public abstract class BaseOperationTest {

  PointCommService pointCommService;

  @Before
  public void init() {
    DataSource dataSource = new PooledDataSource(
        "com.mysql.jdbc.Driver",
        "jdbc:mysql://127.0.0.1:3306/hvmall?autoCommit=true&useUnicode=true&autoReconnect=true&characterEncoding=UTF-8",
        "root",
        "19940413");
    PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
    PointContextInitialize initialize = new PointContextInitialize(dataSource, transactionManager);
    pointCommService = initialize.getPointService();
  }

}
