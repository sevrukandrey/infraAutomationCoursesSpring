package com.playtika.automation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarForSale;
import com.playtika.automation.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(CarsController.class)
public class CarsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private CarService carService;

    @Test
    public void shouldAddCar() throws Exception {
        Car car = getCar();
        when(carService.addCar(anyObject(), anyDouble(), anyString())).thenReturn(1L);

        String contentAsString = mockMvc.perform(post("/car")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(car))
                .param("price", String.valueOf(1000))
                .param("ownerContacts", "Amdre"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn().getResponse().getContentAsString();

        verify(carService, times(1)).addCar(car, 1000, "Amdre");

        assertThat(contentAsString).isEqualTo("1");

    }

    @Test
    public void shouldGetAllCars() throws Exception {

        Map<Long, CarForSale> cars = new HashMap<>();
        cars.put(1L, getCarForSale());



        String actual = cars.toString();

        when(carService.getAllCars()).thenReturn(cars);

        mockMvc.perform(get("/cars")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.yourKeyValue", is("WhatYouExpect")));

//{"2":{"id":2,"model":"2","brand":"1","ownerContacts":"ASdasd","price":1000.0}}
       // assertThat(actual).isEqualTo(resultActions);


        verify(carService, times(1)).getAllCars();



    }


    @Test
    public void deleteCar() throws Exception {
    }

    @Test
    public void getCarDetails() throws Exception {
    }


    private Car getCar() {
        return Car
                .builder()
                .brand("bmw")
                .model("x5")
                .build();
    }

    private CarForSale getCarForSale() {
        CarForSale carForSale = new CarForSale();
        carForSale.setId(2);
        carForSale.setModel("x5");
        carForSale.setBrand("bmw");
        carForSale.setOwnerContacts("Andrey");
        return carForSale;
    }

}