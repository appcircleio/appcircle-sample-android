package com.example.appcircle_sample_android;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources",
    glue = "com.appcircle.appcircle_sample_android",
    plugin = {
        "pretty",
        "json:build/reports/cucumber/cucumber.json" // JSON raporu için eklendi
    },
    tags = "@smoke"
)
public class CucumberTestRunner {
}

