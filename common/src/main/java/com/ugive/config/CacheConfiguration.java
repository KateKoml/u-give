package com.ugive.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.TimeUnit;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "caffeine")
@PropertySource(value = "classpath:application.yml")
public class CacheConfiguration {
    private Integer initialCapacity;
    private Integer maximumSize;
    private Integer expireAfterAccessHours;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("c_offer_status", "c_payment_type",
                "c_product_categories", "c_product_conditions", "roles", "users");
        cacheManager.setCaffeine(cacheProperties());
        return cacheManager;
    }

    public Caffeine<Object, Object> cacheProperties() {
        return Caffeine.newBuilder()
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .expireAfterAccess(expireAfterAccessHours, TimeUnit.HOURS)
                .weakKeys()
                .recordStats();
    }
}