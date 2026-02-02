package com.example.demo.steps;

import com.example.demo.config.ApiClient;
import com.example.demo.config.MockData;
import com.example.demo.config.TestProperties;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import junit.framework.Assert;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("deprecation")
public class UserStepDefinitions {

    @Autowired
    private TestProperties testProperties;

    @Autowired
    private ApiClient client;

    private Response lastResponse;
    private List<Integer> collectedIds = new ArrayList<>();
    private int totalUsers = 0;

    @Given("^I get the default list of users for on (\\d+)st page$")
    public void iGetTheDefaultListOfUsers(int page) {
        if(testProperties.getMockDataTest()) {
            lastResponse = MockData.mockResponse();
        } else {
            lastResponse = client.get("/users", Collections.singletonMap("page", page));
        }
        if (lastResponse != null && lastResponse.getStatusCode() == 200) {
            List<Map<String, Object>> data = lastResponse.jsonPath().getList("data");
            collectedIds.clear();
            for (Map<String, Object> u : data) {
                Number idNum = (Number) u.get("id");
                if (idNum != null) collectedIds.add(idNum.intValue());
            }
            totalUsers = lastResponse.jsonPath().getInt("total");
            System.out.println(">>>>>>>>>>>>>>>>>>>>" + totalUsers);
        }
    }

    @When("^I get the list of all users within every page$")
    public void iGetTheListOfAllUsers() {
        Response r = client.get("/users", Collections.singletonMap("page", 1));
        int totalPages = r.jsonPath().getInt("total_pages");
        collectedIds.clear();
        for (int p = 1; p <= totalPages; p++) {
            Response pageResp = client.get("/users", Collections.singletonMap("page", p));
            List<Map<String, Object>> data = pageResp.jsonPath().getList("data");
            for (Map<String, Object> u : data) {
                Number idNum = (Number) u.get("id");
                if (idNum != null) collectedIds.add(idNum.intValue());
            }
        }
        // total from API (optional)
        totalUsers = r.jsonPath().getInt("total");
    }

    @Then("^I should see total users count equals the number of user ids$")
    public void iShouldMatchTotalCount() {
        Assert.assertEquals("total should equal unique ids count", totalUsers, collectedIds.size());
    }

    @Given("^I make a search for user (\\d+)$")
    public void iMakeSearchForUser(int userId) {
        lastResponse = client.get("/users/" + userId, null);
        if(!Objects.equals(HttpStatus.SC_NOT_FOUND, lastResponse.getStatusCode())) {
            Map<String, Object> dataMap = lastResponse.jsonPath().getMap("data");
            Integer id = (Integer) dataMap.get("id");
            Assert.assertEquals(id, Integer.valueOf(userId));
        }
    }

    @Then("^I should see the following user data$")
    public void iShouldSeeUserData(DataTable dataTable) {
        DataTable.TableConverter tableConverter = dataTable.getTableConverter();

        Map<String, String> data = tableConverter.toMap(dataTable, String.class, String.class);
        Map<String, Object> dataMap = lastResponse.jsonPath().getMap("data");
        String firstName = dataMap.get("first_name").toString();
        String email = dataMap.get("email").toString();
        Assert.assertEquals(data.get("first_name"), firstName);
        Assert.assertEquals(data.get("email"), email);
    }

    @Then("^I receive error code (\\d+) in response$")
    public void errorResponseTest(int errorCode) {
        Assert.assertEquals(errorCode, lastResponse.getStatusCode());
    }

    @Given("^I create a user with following (.*) (.*)$")
    public void iCreateUserWith(String name, String job) {
        String requestBody = String.format("{\"name\":\"%s\",\"job\":\"%s\"}", name, job);
        lastResponse = client.post("/users", requestBody);
    }

    @Then("^response should contain the following data$")
    public void responseShouldContainData(DataTable dataTable) {
        Assert.assertEquals("Response status should be 201", HttpStatus.SC_CREATED, lastResponse.getStatusCode());
        
        List<String> expectedFields = dataTable.row(0);
        
        for (String field : expectedFields) {
            Object fieldValue = lastResponse.jsonPath().get(field);
            Assert.assertNotNull("Field " + field + " should not be null", fieldValue);
        }
    }

    @Given("^I login (successfully|unsuccessfully) with the following data$")
    public void iLoginWithCredentials(String loginType, DataTable dataTable) {
        List<String> credentials = dataTable.row(1);
        String email = credentials.get(0);
        String password = credentials.get(1);
        
        String requestBody = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, null == password ? "" : password);
        lastResponse = client.post("/login", requestBody);
    }

    @Then("^I should get a response code of (\\d+)$")
    public void iShouldGetResponseCode(int expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, lastResponse.getStatusCode());
    }

    @Then("^I should see the following response message:$")
    public void iShouldSeeResponseMessage(DataTable dataTable) {
        List<String> expectedMessages = dataTable.asList();
        String responseBody = lastResponse.getBody().asString();
        String expectedMsg = expectedMessages.toString().replace("\"", "").replace("[","").replace("]","").replace(": ",":");
        String response = responseBody.replace("\"", "").replace("{","").replace("}","");

        Assert.assertTrue("Response should contain: " + expectedMessages,
                response.contains(expectedMsg));
    }

    @Given("^I wait for the user list to load$")
    public void iWaitForUserListToLoad() {
        lastResponse = client.get("/users?delay=3", null);
        Assert.assertEquals("Response should be successful", HttpStatus.SC_OK, lastResponse.getStatusCode());
    }

    @Then("^I should see that every user has a unique id$")
    public void iShouldSeeUniqueUserIds() {
        List<Map<String, Object>> users = lastResponse.jsonPath().getList("data");
        List<Integer> userIds = new ArrayList<>();
        
        for (Map<String, Object> user : users) {
            Integer userId = (Integer) user.get("id");
            Assert.assertNotNull("User ID should not be null", userId);
            Assert.assertFalse("User ID should be unique", userIds.contains(userId));
            userIds.add(userId);
        }
    }

}