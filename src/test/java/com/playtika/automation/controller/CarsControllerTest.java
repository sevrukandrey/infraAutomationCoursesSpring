package com.playtika.automation.controller;

import com.playtika.automation.service.CarService;
import org.mockito.Mockito;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(CarsController.class)
public class CarsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    private CarsController carsController = new CarsController(carService);


    @Test
    public void shouldAddCar() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carsController).build();
       when(carService.addCar(anyObject(),anyDouble(),anyString())).thenReturn(1L);

        double price = 1.0;
        mockMvc.perform(post("/car")
                .contentType("application/json")
               .content("{'brand':'bww','model':'x5'}")
               .param("price", String.valueOf(price))
               .param("ownerContacts","Amdre")

       ).andReturn().getResponse().;
    }

    @Test
    public void getAllCars() throws Exception {
    }

    @Test
    public void deleteCar() throws Exception {
    }

    @Test
    public void getCarDetails() throws Exception {
    }

}