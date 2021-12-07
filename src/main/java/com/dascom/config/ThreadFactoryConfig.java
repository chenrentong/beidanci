package com.dascom.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@EnableAsync
@Configuration
@PropertySource("classpath:threadpool.properties")
public class ThreadFactoryConfig {
 	
	@Value("${thread.pool.corePoolSize}")
	private Integer corePoolSize;
	
	@Value("${thread.pool.maxPoolSize}")
	private Integer maxPoolSize;
	
	@Value("${thread.pool.keepAliveTime}")
	private Integer keepAliveTime;
	
	@Value("${thread.pool.dequeCapacity}")
	private Integer queueCapacity;
	
    @Bean
    public ThreadPoolTaskExecutor asyncPool() {
    	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    	executor.setCorePoolSize(corePoolSize);
    	executor.setMaxPoolSize(maxPoolSize);
    	executor.setKeepAliveSeconds(keepAliveTime);
    	executor.setThreadPriority(Thread.NORM_PRIORITY);
    	executor.setQueueCapacity(queueCapacity);
    	executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    	executor.initialize();
        return executor;
    }
}