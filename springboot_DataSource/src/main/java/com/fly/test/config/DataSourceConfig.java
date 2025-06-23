package com.fly.test.config;


import com.fly.test.context.DataSourceContextHolder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        DataSource dataSource = DataSourceBuilder.create().build();
        return dataSource;
    }

    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        DataSource dataSource = DataSourceBuilder.create().build();
        return dataSource;
    }

    @Bean
    @Primary
    public DataSource testDataSource() {
        DataSourceRouter dataSourceRouter = new DataSourceRouter();
        Map<Object, Object> dataSourcesMap = new HashMap<>();
        dataSourcesMap.put("primaryDataSource", primaryDataSource());
        dataSourcesMap.put("secondaryDataSource", secondaryDataSource());
        dataSourceRouter.setDefaultTargetDataSource(primaryDataSource());
        dataSourceRouter.setTargetDataSources(dataSourcesMap);
        return dataSourceRouter;
    }
}