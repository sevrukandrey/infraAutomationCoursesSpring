package com.playtika.automation.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarSaleInfo;
import com.playtika.automation.domain.SaleInfo;
import com.playtika.automation.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(CarsController.class)
public class CarsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private CarService carService;

//    @Test
//    public void shouldAddCar() throws Exception {
//
//        when(carService.addCar(getCar(), 1000, "Andrey")).thenReturn(1L);
//
//        String contentAsString = mockMvc.perform(post("/cars")
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                .content("{\"brand\": \"ford\",\"model\":\"fiesta\"}")
//                .param("price", String.valueOf(1000.0))
//                .param("ownerContacts", "Andrey"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        verify(carService).addCar(getCar(), 1000, "Andrey");
//
//        assertThat(contentAsString).isEqualTo("1");
//    }
//
//
//    @Test
//    public void shouldGetAllCars() throws Exception {
//
//        when(carService.getAllCars()).thenReturn(Collections.singletonList(constructCarsForSale()));
//
//        mockMvc.perform(get("/cars")
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.[0].id", is(1)))
//                .andExpect(jsonPath("$.[0]car.model", is("fiesta")))
//                .andExpect(jsonPath("$.[0]car.brand", is("ford")))
//                .andExpect(jsonPath("$.[0]saleInfo.price", is(1000.0)))
//                .andExpect(jsonPath("$.[0]saleInfo.ownerContacts", is("Andrey")));
//
//        verify(carService).getAllCars();
//    }
//
//    @Test
//    public void shouldSuccessfullyResponseIfGetAllCarsEmpty() throws Exception {
//
//        when(carService.getAllCars()).thenReturn(new ArrayList<>());
//
//        mockMvc.perform(get("/cars")
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"));
//
//        verify(carService).getAllCars();
//    }
//
//
//    @Test
//    public void shouldDeleteCar() throws Exception {
//        doNothing().when(carService).deleteCar(1L);
//        mockMvc.perform(delete("/cars/1")
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(status().isOk());
//
//        verify(carService).deleteCar(1L);
//    }
//
//    @Test
//    public void shouldReturnCarSaleInfo() throws Exception {
//        SaleInfo carInfo = new SaleInfo("Andrey",1000.0);
//
//        when(carService.getSaleInfo(1L)).thenReturn(of(carInfo));
//
//        mockMvc.perform(get("/cars/1")
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.ownerContacts", is("Andrey")))
//                .andExpect(jsonPath("$.price", is(1000.0)));
//
//        verify(carService, times(1)).getSaleInfo(1);
//    }
//
//    @Test
//    public void shouldReturnNotFounfIfCarSaleInfoIsEmpty() throws Exception {
//        mockMvc.perform(get("/cars/1").accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//
//
//    private Car getCar() {
//        return new Car("ford", "fiesta");
//    }
//
//    private CarSaleInfo constructCarsForSale() {
//        return new CarSaleInfo(1, getCar(), new SaleInfo("Andrey", 1000));
//    }

}