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
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(Long.valueOf(id)).isGreaterThan(0L);
    }


    @Test
    public void shouldReturnAllCars() throws Exception {

        mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{\"brand\": \"Ford\",\"model\":\"fiesta\"}")
                .param("price", String.valueOf(1000))
                .param("ownerContacts", "Andrey"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/cars")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.[0]car.model", is("fiesta")))
                .andExpect(jsonPath("$.[0]car.brand", is("Ford")))
                .andExpect(jsonPath("$.[0]saleInfo.price", is(1000.0)))
                .andExpect(jsonPath("$.[0]saleInfo.ownerContacts", is("Andrey")));
    }

    @Test
    public void shouldDeleteCarById() throws Exception {
        mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{\"brand\": \"Ford\",\"model\":\"fiesta\"}")
                .param("price", String.valueOf(1000))
                .param("ownerContacts", "Amdre"))
                .andExpect(status().isOk());

       mockMvc.perform(delete("/cars/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());

       String result =  mockMvc.perform(get("/cars")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

       assertThat(result).isEqualTo("[]");
    }

    @Test
    public void shouldGetSaleInfoByCarId() throws Exception {
        mockMvc.perform(get("/cars/2/")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.ownerContacts", is("Andrey")))
                .andExpect(jsonPath("$.price", is(1000.0)))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

}
