package cn.com.clickhouse.config;

import cn.com.clickhouse.pojo.UisTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.alibaba.druid.pool.DruidDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: wanqh
 * @date: created in 2019/10/9 20:31
 * @version:
 * @since:
 */
@Configuration
@Slf4j
public class ClickHouseDbConfig {

    @Resource
    private JdbcParamConfig jdbcParamConfig;
    @Value("${insertDataQueueSize:200}")
    private int insertDataQueueSize;
    @Value("${insertBathPoolSize:50}")
    private int insertBathPoolSize;


    @Bean
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(jdbcParamConfig.getUrl());
        datasource.setDriverClassName(jdbcParamConfig.getDriverClassName());
        datasource.setInitialSize(jdbcParamConfig.getInitialSize());
        datasource.setMinIdle(jdbcParamConfig.getMinIdle());
        datasource.setMaxActive(jdbcParamConfig.getMaxActive());
        datasource.setMaxWait(jdbcParamConfig.getMaxWait());
        datasource.setUsername(jdbcParamConfig.getUsername());
        datasource.setPassword(jdbcParamConfig.getPassword());
        datasource.setTestWhileIdle(false);
        return datasource;
    }


    @Bean(destroyMethod = "shutdown")
    public ThreadPoolExecutor insertBathPool() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(insertBathPoolSize);
        return executor;
    }

    @Bean
    public BlockingQueue<List<UisTest>> insertDataQueues(){
        BlockingQueue<List<UisTest>> queue = new LinkedBlockingQueue<List<UisTest>>(insertDataQueueSize);
        return queue;
    }
}
