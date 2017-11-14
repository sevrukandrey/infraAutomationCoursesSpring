package com.playtika.automation.web;

import com.playtika.automation.service.CarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)

public class CarControllerTest {

    @Mock
    private CarService carService;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new CarsController(carService))
            .build();
    }

    @Test
    public void addCarShouldGetAndResponseInJson() throws Exception {
        when(carService.addCar(anyObject(), anyDouble(), anyString())).thenReturn(1L);

        mockMvc.perform(post("/car")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content("{\"brand\": \"Ford\",\"model\":\"fiesta\"}")
            .param("price", String.valueOf(1000))
            .param("ownerContacts", "Amdre"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(content().json("1"));
    }

    @Test
    public void addCarShouldSendBadRequestOnMissingParams() throws Exception {
        mockMvc.perform(post("/car").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteCarShouldSendBadRequestOnMissingParams() throws Exception {
        mockMvc.perform(delete("/car").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void getCarDetailsShouldSendBadRequestOnMissingParams() throws Exception {
        mockMvc.perform(get("/car").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllCarsShouldGetAndResponseInJson() throws Exception {

        mockMvc.perform(get("/cars")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void deleteCarsShouldGetAndResponseInJson() throws Exception {

        mockMvc.perform(delete("/car")
            .param("id", String.valueOf(1))
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk());
    }

    @Test
    public void getCarDetailsShouldGetAndResponseInJson() throws Exception {

        mockMvc.perform(get("/car")
            .param("id", String.valueOf(1))
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk());
    }
}
