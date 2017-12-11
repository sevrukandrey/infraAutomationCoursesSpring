package com.playtika.automation.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static java.lang.Long.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content("{\"brand\": \"Ford\",\"model\":\"fiesta\"}")
                .param("price", "1000")
                .param("ownerContacts", "Amdre"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(valueOf(id)).isGreaterThan(0L);
    }

    @Test
    public void shouldReturnAllCars() throws Exception {
        mockMvc.perform(post("/cars")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content("{\"brand\": \"Ford\",\"model\":\"fiesta\"}")
                .param("price", "1000")
                .param("ownerContacts", "Andrey"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/cars")
                .contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[0]car.model", is("fiesta")))
                .andExpect(jsonPath("$.[0]car.brand", is("Ford")))
                .andExpect(jsonPath("$.[0]saleInfo.price", is(1000.0)))
                .andExpect(jsonPath("$.[0]saleInfo.ownerContacts", is("Andrey")));
    }

    @Test
    public void shouldDeleteCarById() throws Exception {
        mockMvc.perform(post("/cars")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content("{\"brand\": \"Ford\",\"model\":\"fiesta\"}")
                .param("price", "1000")
                .param("ownerContacts", "Amdre"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/cars/1")
                .contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());

        String result = mockMvc.perform(get("/cars")
                .contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo("[]");
    }

    @Test
    public void shouldGetSaleInfoByCarId() throws Exception {
        mockMvc.perform(get("/cars/2/")
                .contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.ownerContacts", is("Andrey")))
                .andExpect(jsonPath("$.price", is(1000.0)))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

}
