package com.xquark.config;import com.xquark.service.Scanned;import org.springframework.context.annotation.ComponentScan;import org.springframework.context.annotation.Configuration;import org.springframework.context.annotation.ImportResource;import org.springframework.transaction.annotation.EnableTransactionManagement;@Configuration@ComponentScan(basePackageClasses = Scanned.class)@EnableTransactionManagement@ImportResource({"classpath:/META-INF/applicationContext-service.xml"})// 启用CGLIB动态代理public class PointServiceConfig {}