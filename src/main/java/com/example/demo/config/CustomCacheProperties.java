package com.example.demo.config;


import lombok.Data;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix="daemon.cache")
public class CustomCacheProperties {

    private Map<String, CacheProperties.Redis> customCache;

}
