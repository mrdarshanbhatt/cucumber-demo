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
        if(HttpStatus.SC_NOT_FOUND == lastResponse.getStatusCode()) {
            System.out.println("User with ID " + userId + " not found.");
        }
        Map<String, Object> dataMap = lastResponse.jsonPath().getMap("data");
        Integer id = (Integer) dataMap.get("id");
        Assert.assertEquals(id, Integer.valueOf(userId));
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

    @Then("^I receive error code (\\d+)$ in response")
    public void errorResponseTest(int errorCode) {
        Assert.assertEquals(HttpStatus.SC_NOT_FOUND, errorCode);
    }

}