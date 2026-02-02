package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api")
public class TestProperties {

    private String baseUrl;
    private int timeout;
    private int retryCount;
    private String environment;
    private boolean mockDataEnabled;
    private String apiKey;
    private String apiKeyValue;
    
    // Getters and Setters
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
    
    public boolean isMockDataEnabled() { return mockDataEnabled; }
    public void setMockDataEnabled(boolean mockDataEnabled) { this.mockDataEnabled = mockDataEnabled; }
    
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    
    public String getApiKeyValue() { return apiKeyValue; }
    public void setApiKeyValue(String apiKeyValue) { this.apiKeyValue = apiKeyValue; }
    
    public Boolean getMockDataTest() { return mockDataEnabled; }
}
