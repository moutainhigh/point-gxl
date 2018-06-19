package com.hds.xquark.config;

import com.hds.xquark.dal.mapper.CommissionRecordMapper;
import com.hds.xquark.dal.mapper.CommissionTotalAuditMapper;
import com.hds.xquark.dal.mapper.CommissionTotalMapper;
import com.hds.xquark.dal.mapper.GradeCodeMapper;
import com.hds.xquark.dal.mapper.PointRecordMapper;
import com.hds.xquark.dal.mapper.PointTotalAuditMapper;
import com.hds.xquark.dal.mapper.PointTotalMapper;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.Resource;


@Configuration
@ImportResource("classpath:/META-INF/app-context-dal.xml")
public class PointDalConfig {

  @Value("classpath:config/MapperPointConfig.xml")
  Resource mybatisMapperConfig;

  @Autowired
  DataSource dataSource;

  @Bean
  public GradeCodeMapper pointGradeMapper() throws Exception {
    return newMapperFactoryBean(GradeCodeMapper.class).getObject();
  }

  @Bean
  public PointRecordMapper pointRecordMapper() throws Exception {
    return newMapperFactoryBean(PointRecordMapper.class).getObject();
  }

  @Bean
  public CommissionRecordMapper commissionRecordMapper() throws Exception {
    return newMapperFactoryBean(CommissionRecordMapper.class).getObject();
  }

  @Bean
  public PointTotalMapper pointInfoMapper() throws Exception {
    return newMapperFactoryBean(PointTotalMapper.class).getObject();
  }

  @Bean
  public CommissionTotalMapper commissionTotalMapper() throws Exception {
    return newMapperFactoryBean(CommissionTotalMapper.class).getObject();
  }

  @Bean
  public PointTotalAuditMapper pointTotalAuditMapper() throws Exception {
    return newMapperFactoryBean(PointTotalAuditMapper.class).getObject();
  }

  @Bean
  public CommissionTotalAuditMapper commissionTotalAuditMapper() throws Exception {
    return newMapperFactoryBean(CommissionTotalAuditMapper.class).getObject();
  }

  // @Autowired
  // Environment env;

  private <T> MapperFactoryBean<T> newMapperFactoryBean(Class<T> clazz) throws Exception {
    final MapperFactoryBean<T> b = new MapperFactoryBean<T>();
    b.setMapperInterface(clazz);
    b.setSqlSessionFactory(sqlSessionFactory());
    return b;
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {
    final SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
    fb.setConfigLocation(mybatisMapperConfig);
    fb.setDataSource(dataSource);
    // env.acceptsProfiles("prod") ? IdTypeHandler.class : IdTypeNullHandler.class
//    fb.setTypeAliases(new Class<?>[]{IdTypeHandler.class, ObjectRangeHandler.class,
//            PromotionActionTypeHandler.class, DomainTypeHandler.class});
    return fb.getObject();
  }
}
