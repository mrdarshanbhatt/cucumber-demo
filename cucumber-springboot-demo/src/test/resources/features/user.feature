Feature: API test
# Please visit https://reqres.in/

  Scenario: Should see LIST USERS of all existing users
    Given I get the default list of users for on 1st page
    When I get the list of all users within every page
    Then I should see total users count equals the number of user ids

  Scenario: Should see SINGLE USER data
    Given I make a search for user 3
    Then I should see the following user data
      | first_name  | Emma                |
      | email       | emma.wong@reqres.in |

  Scenario: Should see SINGLE USER NOT FOUND error code
    Given I made a search for user 55
    Then I receive error code 404 in response