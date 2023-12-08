package com.bootcamp48.java.movement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class ReactiveConfig {

    @Bean
    public Scheduler scheduler() {
        return Schedulers.boundedElastic();
    }
}

