package com.example.demo.config;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiClient {

    @Autowired
    private TestProperties testProperties;

    public ApiClient(String baseUri) {
        RestAssured.baseURI = baseUri;
    }

    public Response get(String path, Map<String, ?> params) {
        if (params == null || params.isEmpty()) {
            return RestAssured.given().header(testProperties.getApiKey(), testProperties.getApiKeyValue()).contentType(ContentType.JSON).when().get(path).andReturn();
        }
        return RestAssured.given().queryParams(params).header(testProperties.getApiKey(), testProperties.getApiKeyValue()).contentType(ContentType.JSON).when().get(path).andReturn();
    }

    public Response post(String path, Object body) {
        return RestAssured.given().header(testProperties.getApiKey(), testProperties.getApiKeyValue()).contentType(ContentType.JSON).body(body).when().post(path).andReturn();
    }
}
