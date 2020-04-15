package com.gulang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@SpringBootApplication
@EnableScheduling  // 开启定时任务
@EnableCaching  // 开启缓存 redis需要
public class CpmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CpmsApplication.class, args);
    }

}
