package cn.com.clickhouse;

import cn.com.clickhouse.service.UisTestService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"cn.com.clickhouse.mapper"})
public class ClickhouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClickhouseApplication.class, args);
    }
}
