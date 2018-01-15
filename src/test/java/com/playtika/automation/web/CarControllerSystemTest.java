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
        Long id = postCar();

        assertThat(id).isGreaterThan(0);

    }

    @Test
    public void shouldReturnAllCars() throws Exception {
        postCar();

        mockMvc.perform(get("/cars")
            .contentType(APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[0]car.model", is("fiesta")))
            .andExpect(jsonPath("$.[0]car.brand", is("ford")))
            .andExpect(jsonPath("$.[0]saleInfo.price", is(1000.0)))
            .andExpect(jsonPath("$.[0]saleInfo.ownerContacts", is("093")));
    }

    @Test
    public void shouldPutCarOnSale() throws Exception {
        String putCarForSale = "{\"brand\": \"ford\",\"model\":\"fiesta\",\"plateNumber\":\"12-22\"," +
                "\"year\":\"1212\",\"color\":\"green\"," +
                "\"name\":\"Andrey\",\"sureName\":\"Sevruk\",\"phoneNumber\":\"093\",\"price\":\"1000\"}";

        String contentAsString = mockMvc.perform(put("/car")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content(putCarForSale))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE)).andReturn().getResponse().getContentAsString();

        assertThat(Long.valueOf(contentAsString)).isEqualTo(2L);
    }


    @Test
    public void shouldDeleteCarById() throws Exception {
        Long id = postCar();

        mockMvc.perform(delete("/cars/" + id)
            .contentType(APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldGetSaleInfoByCarId() throws Exception {
        Long id = postCar();

        mockMvc.perform(get("/cars/" + id)
            .contentType(APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.ownerContacts", is("Andrey")))
            .andExpect(jsonPath("$.price", is(1000.0)))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }


    @Test
    public void shouldRejectDealById() throws Exception {
        //add deal and get Id should implement endpoint

        mockMvc.perform(post("/rejectDeal/1")
                .contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private Long postCar() throws Exception {
        String id = mockMvc.perform(post("/cars")
            .contentType(APPLICATION_JSON_UTF8_VALUE)
            .content("{\"brand\": \"Ford\",\"model\":\"fiesta\"}")
            .param("price", "1000")
            .param("ownerContacts", "Andrey"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
            .andReturn()
            .getResponse()
            .getContentAsString();
        return Long.valueOf(id);
    }
}
