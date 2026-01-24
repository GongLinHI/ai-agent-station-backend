package com.gonglin.ai4knowledge.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "thread.pool.executor", ignoreInvalidFields = true)
public class ThreadPoolConfigProperties {

    private static final Integer DEFAULT_CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() / 2;
    private static final Integer DEFAULT_MAX_POOL_SIZE = 2 * DEFAULT_CORE_POOL_SIZE;
    /**
     * 核心线程数（默认：CPU核心数）
     */
    private Integer corePoolSize = DEFAULT_CORE_POOL_SIZE;
    /**
     * 最大线程数（默认：CPU核心数 * 2）
     */
    private Integer maxPoolSize = DEFAULT_MAX_POOL_SIZE;
    /**
     * 最大等待时间
     */
    private Long keepAliveTime = 10L;
    /**
     * 最大队列数
     */
    private Integer blockQueueSize = 5000;
    /*
     * AbortPolicy：丢弃任务并抛出RejectedExecutionException异常。
     * DiscardPolicy：直接丢弃任务，但是不会抛出异常
     * DiscardOldestPolicy：将最早进入队列的任务删除，之后再尝试加入队列的任务被拒绝
     * CallerRunsPolicy：如果任务添加线程池失败，那么主线程自己执行该任务
     * */
    private String policy = "AbortPolicy";
}
