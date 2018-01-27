package com.playtika.automation.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Configuration
public class TimeBonusFixtureConfiguration {

    @Bean
    MockMvc mockMvc(WebApplicationContext webApplicationContext) {
        return webAppContextSetup(webApplicationContext).build();
    }

}