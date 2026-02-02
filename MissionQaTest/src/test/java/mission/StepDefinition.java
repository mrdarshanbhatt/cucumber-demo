package mission;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.DataTable;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;

public class StepDefinition {

    private ApiClient client;
    private Response lastResponse;
    private List<Integer> collectedIds = new ArrayList<>();
    private int totalUsers = 0;
    boolean isMockDataTest = true;

    @Given("^I am on the home page$")
    public void iAmOnTheHomePage() {
        client = new ApiClient(LoadProp.prop.getProperty("base-url"));
        isMockDataTest = Boolean.parseBoolean(LoadProp.prop.get("mock-data-test").toString());
        HomePage.homePage();
    }

    @Given("^I get the default list of users for on (\\d+)st page$")
    public void iGetTheDefaultListOfUsers(int page) {
        if(isMockDataTest) {
            lastResponse = client.get("/users", Collections.singletonMap("page", page));
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
        client = new ApiClient(LoadProp.prop.getProperty("base-url"));
        lastResponse = client.get("/users/" + userId, null);
    }

    @Then("^I should see the following user data$")
    public void iShouldSeeUserData(DataTable dataTable) {
        // Implementation for user data validation
    }

    @Then("^I receive error code (\\d+) in response$")
    public void iReceiveErrorCode(int errorCode) {
        Assert.assertEquals(errorCode, lastResponse.getStatusCode());
    }

    @Given("^I create a user with following (.+) (.+)$")
    public void iCreateUser(String name, String job) {
        client = new ApiClient(LoadProp.prop.getProperty("base-url"));
        Map<String, String> map = new HashMap();
        map.put("name", name);
        map.put("job", job);
        lastResponse = client.post("/users", map);
    }

    @Then("^response should contain the following data$")
    public void responseShouldContainData(DataTable dataTable) {
        // Implementation for response validation
    }

    @Given("^I login unsuccessfully with the following data$")
    public void iLoginUnsuccessfully(DataTable dataTable) {
        client = new ApiClient(LoadProp.prop.getProperty("base-url"));
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        lastResponse = client.post("/login", data.get(0));
    }

    @Then("^I should get a response code of (\\d+)$")
    public void iShouldGetResponseCode(int responseCode) {
        Assert.assertEquals(responseCode, lastResponse.getStatusCode());
    }

    @Then("^I should see the following response message:$")
    public void iShouldSeeResponseMessage(DataTable dataTable) {
        // Implementation for response message validation
    }

    @Given("^I wait for the user list to load$")
    public void iWaitForUserListToLoad() {
        client = new ApiClient(LoadProp.prop.getProperty("base-url"));
        lastResponse = client.get("/users?delay=3", null);
    }

    @Then("^I should see that every user has a unique id$")
    public void iShouldSeeUniqueIds() {
        List<Map<String, Object>> data = lastResponse.jsonPath().getList("data");
        List<Integer> ids = new ArrayList<>();
        for (Map<String, Object> user : data) {
            ids.add((Integer) user.get("id"));
        }
        Assert.assertEquals("All IDs should be unique", ids.size(), ids.stream().distinct().count());
    }
}
