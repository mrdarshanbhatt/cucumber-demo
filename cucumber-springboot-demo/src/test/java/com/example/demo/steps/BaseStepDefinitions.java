package com.example.demo.steps;

import com.example.demo.config.ApiClient;
import com.example.demo.config.TestProperties;
import com.example.demo.data.TestDataManager;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import junit.framework.Assert;
import org.apache.http.HttpStatus;

@SuppressWarnings("deprecation")
public abstract class BaseStepDefinitions {
    
    @Autowired
    protected TestProperties testProperties;
    
    @Autowired
    protected ApiClient client;
    
    @Autowired
    protected TestDataManager testDataManager;
    
    protected Response lastResponse;
    
    protected void validateStatusCode(int expectedStatusCode) {
        Assert.assertEquals("Status code mismatch", expectedStatusCode, lastResponse.getStatusCode());
    }
    
    protected void validateResponseNotNull() {
        Assert.assertNotNull("Response should not be null", lastResponse);
    }
    
    protected void validateSuccessResponse() {
        validateStatusCode(HttpStatus.SC_OK);
    }
}