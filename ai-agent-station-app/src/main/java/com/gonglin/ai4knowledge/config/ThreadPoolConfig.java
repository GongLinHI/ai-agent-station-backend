package com.gonglin.ai4knowledge.config;

import com.gonglin.ai4knowledge.property.ThreadPoolConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableAsync
@Configuration
@EnableConfigurationProperties(ThreadPoolConfigProperties.class) // 启用配置属性绑定
public class ThreadPoolConfig {
    @Bean
    @ConditionalOnMissingBean(ThreadPoolExecutor.class)
    public ThreadPoolExecutor threadPoolExecutor(
            ThreadPoolConfigProperties properties) {
        // 实例化策略
        RejectedExecutionHandler handler = switch (properties.getPolicy()) {
            case "DiscardPolicy" -> new ThreadPoolExecutor.DiscardPolicy();
            case "DiscardOldestPolicy" -> new ThreadPoolExecutor.DiscardOldestPolicy();
            case "CallerRunsPolicy" -> new ThreadPoolExecutor.CallerRunsPolicy();
            default -> new ThreadPoolExecutor.AbortPolicy();
        };
        // 创建线程池
        return new ThreadPoolExecutor(properties.getCorePoolSize(),
                                      properties.getMaxPoolSize(),
                                      properties.getKeepAliveTime(),
                                      TimeUnit.SECONDS,
                                      new LinkedBlockingQueue<>(properties.getBlockQueueSize()),
                                      Executors.defaultThreadFactory(),
                                      handler);
    }
}
