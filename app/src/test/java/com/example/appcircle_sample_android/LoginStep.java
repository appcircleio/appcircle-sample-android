package com.example.appcircle_sample_android;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class LoginSteps {

    @Given("the user is on the login screen")
    public void theUserIsOnLoginScreen() {
        // Login ekranının yüklendiğini kontrol edin
    }

    @When("the user enters valid credentials")
    public void theUserEntersValidCredentials() {
        // Kullanıcının geçerli giriş bilgilerini girdiğini simüle edin
    }

    @Then("the user is navigated to the home screen")
    public void theUserIsNavigatedToTheHomeScreen() {
        // Kullanıcının ana ekrana yönlendirildiğini doğrulayın
    }
}
