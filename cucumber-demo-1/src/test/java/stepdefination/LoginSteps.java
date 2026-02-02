package stepdefination;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;

public class LoginSteps {
    private ApiClient client;
    private Response lastResponse;
    private List<Integer> collectedIds = new ArrayList<>();
    private int totalUsers = 0;


    @Given("^I am on the home page$")
    public void iAmOnTheHomePage() {
        client = new ApiClient("https://reqres.in/api");
    }

    @When("user enters valid credentials")
    public void user_enters_valid_credentials() {
        System.out.println("User enters valid credentials");
    }

    @Then("user should be logged in successfully")
    public void user_should_be_logged_in_successfully() {
        System.out.println("User logged in successfully");
    }
}
