package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Autowired
    private TestProperties testProperties;

    @Bean
    public ApiClient apiClient() {
        return new ApiClient(testProperties.getBaseUrl());
    }
}
