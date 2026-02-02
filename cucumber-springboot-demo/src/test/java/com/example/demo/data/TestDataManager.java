package com.example.demo.data;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.HashMap;

@Component
public class TestDataManager {
    
    private static final Map<String, Map<String, String>> TEST_USERS = new HashMap<>();
    
    static {
        // Valid login credentials
        Map<String, String> validUser = new HashMap<>();
        validUser.put("email", "eve.holt@reqres.in");
        validUser.put("password", "cityslicka");
        TEST_USERS.put("valid_user", validUser);
        
        // Invalid login credentials
        Map<String, String> invalidUser = new HashMap<>();
        invalidUser.put("email", "eve.holt@reqres.in");
        invalidUser.put("password", "");
        TEST_USERS.put("invalid_user", invalidUser);
        
        // Test user data
        Map<String, String> testUser = new HashMap<>();
        testUser.put("first_name", "Emma");
        testUser.put("email", "emma.wong@reqres.in");
        TEST_USERS.put("test_user_3", testUser);
    }
    
    public Map<String, String> getUser(String userType) {
        return TEST_USERS.get(userType);
    }
    
    public String getUserEmail(String userType) {
        return TEST_USERS.get(userType).get("email");
    }
    
    public String getUserPassword(String userType) {
        return TEST_USERS.get(userType).get("password");
    }
}