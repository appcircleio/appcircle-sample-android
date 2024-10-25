Feature: Login Functionality

  Scenario: Successful login
    Given the user is on the login screen
    When the user enters valid credentials
    Then the user is navigated to the home screen
