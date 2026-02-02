package com.example.demo.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@CucumberContextConfiguration
@SpringBootTest(classes = TestConfig.class)
@EnableConfigurationProperties(TestProperties.class)
public class CucumberSpringConfig {

}