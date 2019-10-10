package cn.com.clickhouse.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: bangsun
 * @date: created in 2019/10/10 19:57
 * @version:
 * @since:
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource.click")
@Data
public class JdbcParamConfig {
    private String username;
    private String password;
    private String driverClassName ;
    private String url ;
    private Integer initialSize ;
    private Integer maxActive ;
    private Integer minIdle ;
    private Integer maxWait ;
}
