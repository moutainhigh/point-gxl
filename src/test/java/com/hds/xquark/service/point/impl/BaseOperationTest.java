package com.hds.xquark.service.point.impl;

import com.hds.xquark.PointContextInitialize;
import com.hds.xquark.dal.model.BasePointCommRecord;
import com.hds.xquark.dal.model.BasePointCommTotal;
import com.hds.xquark.service.point.CommissionService;
import com.hds.xquark.service.point.PointCommService;
import com.hds.xquark.service.point.PointService;
import com.hds.xquark.service.point.TokenService;
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

  private PointContextInitialize initialize;

  private PointService pointServiceApi;

  private CommissionService commissionServiceApi;

  @Before
  public void init() {
    DataSource dataSource = new PooledDataSource(
        "com.mysql.jdbc.Driver",
        "jdbc:mysql://rm-uf6ll0b4hoso96m0n0o.mysql.rds.aliyuncs.com:3306/hvmall?autoCommit=true&useUnicode=true&autoReconnect=true&characterEncoding=UTF-8",
        "devhwscrwa",
        "v7&#5efr&777");
    PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
    this.initialize = new PointContextInitialize(dataSource, transactionManager);
    pointCommService = this.initialize.getPointService();

    this.pointServiceApi = this.initialize.getPointServiceApi();
    this.commissionServiceApi = this.initialize.getCommissionServiceApi();
  }

  public PointContextInitialize getInitialize() {
    return initialize;
  }

  public PointService getPointServiceApi() {
    return pointServiceApi;
  }

  public CommissionService getCommissionServiceApi() {
    return commissionServiceApi;
  }
}
