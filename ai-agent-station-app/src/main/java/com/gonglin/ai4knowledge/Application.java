package com.gonglin.ai4knowledge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.gonglin.ai4knowledge") // 扫描 com.gonglin.ai4knowledge 包下的所有类
@EnableTransactionManagement // 开启注解方式的事务管理
//@ConfigurationPropertiesScan // 开启配置属性扫描
@EnableCaching // Spring Cache
@EnableScheduling // Spring Task
@Slf4j
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("AI Agent Station Application started successfully.");
    }
}
