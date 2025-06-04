package com.cachingdemo.simplepersonproject.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncExecutorConfig {

    @Bean("personThreadPool")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);        // # of threads to keep alive
        executor.setMaxPoolSize(10);        // max # of threads
        executor.setQueueCapacity(100);     // max queue size before rejecting
        executor.setThreadNamePrefix("person-thread-");
        executor.initialize();
        return executor;
    }
}
