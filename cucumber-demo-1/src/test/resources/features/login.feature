Feature: API test
# Please visit https://reqres.in/

  Scenario: Should see LIST USERS of all existing users
    Given I get the default list of users for on 1st page
    When I get the list of all users within every page
    Then I should see total users count equals the number of user ids