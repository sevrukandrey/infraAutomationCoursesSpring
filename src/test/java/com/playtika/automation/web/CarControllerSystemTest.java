package com.playtika.automation.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CarControllerSystemTest {

    @Autowired
    private WebApplicationContext context;


    private MockMvc mockMvc;

    @Before
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void shouldAddCarAndReturnId() throws Exception {
        String id = mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{\"brand\": \"Ford\",\"model\":\"fiesta\"}")
                .param("price", String.valueOf(1000))
                .param("ownerContacts", "Amdre"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8")).andReturn().
                        getResponse()
                .getContentAsString();

        assertThat(id).isEqualTo("1");

    }


    @Test
    public void shouldReturnAllCars() throws Exception {
        String result = mockMvc.perform(get("/cars")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8")).andReturn().getResponse().getContentAsString();

        assertThat(result).isNotEmpty();
    }

    @Test
    public void shouldDeleteCarById() throws Exception {
       mockMvc.perform(delete("/cars/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetSaleInfoByCarId() throws Exception {
        mockMvc.perform(get("/cars/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

}
