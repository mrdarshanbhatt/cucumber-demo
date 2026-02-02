package com.example.demo.runner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.example.demo.steps", "com.example.demo.config"},
        plugin = {"pretty", "html:target/cucumber-report.html"}
)
public class CucumberTestRunner {
}