package com.playtika.automation.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtika.automation.domain.*;
import com.playtika.automation.service.CarService;
import com.playtika.automation.web.exceptions.DealNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static java.util.Collections.singletonList;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
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

    @Test
    public void shouldAddCar() throws Exception {
        when(carService.addCar(constructCar(), 1000, "Andrey")).thenReturn(1L);

        String contentAsString = mockMvc.perform(post("/cars")
            .contentType(APPLICATION_JSON_UTF8_VALUE)
            .content("{\"brand\": \"ford\",\"model\":\"fiesta\",\"plateNumber\":\"12-22\",\"year\":\"1212\",\"color\":\"green\"}")
            .param("price", "1000")
            .param("ownerContacts", "Andrey"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
            .andReturn()
            .getResponse()
            .getContentAsString();

        verify(carService).addCar(constructCar(), 1000, "Andrey");

        assertThat(contentAsString).isEqualTo("1");
    }

    @Test
    public void shouldGetAllCars() throws Exception {
        when(carService.getAllCars()).thenReturn(singletonList(constructCarsForSale()));

        mockMvc.perform(get("/cars")
            .contentType(APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.[0].id", is(1)))
            .andExpect(jsonPath("$.[0]car.model", is("fiesta")))
            .andExpect(jsonPath("$.[0]car.brand", is("ford")))
            .andExpect(jsonPath("$.[0]saleInfo.price", is(1000.0)))
            .andExpect(jsonPath("$.[0]saleInfo.ownerContacts", is("Andrey")));

        verify(carService).getAllCars();
    }

    @Test
    public void shouldSuccessfullyResponseIfGetAllCarsEmpty() throws Exception {
        when(carService.getAllCars()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/cars")
            .contentType(APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"));

        verify(carService).getAllCars();
    }

    @Test
    public void shouldDeleteCar() throws Exception {
        mockMvc.perform(delete("/cars/1")
            .contentType(APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk());

        verify(carService).deleteCar(1L);
    }

    @Test
    public void shouldReturnCarSaleInfo() throws Exception {
        SaleInfo carInfo = new SaleInfo("Andrey", 1000.0);

        when(carService.getSaleInfo(1L)).thenReturn(of(carInfo));

        mockMvc.perform(get("/cars/1")
            .contentType(APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.ownerContacts", is("Andrey")))
            .andExpect(jsonPath("$.price", is(1000.0)));

        verify(carService).getSaleInfo(1);
    }

    @Test
    public void shouldReturnNotFoundIfCarSaleInfoIsEmpty() throws Exception {
        mockMvc.perform(get("/cars/1").accept(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }


    @Test
    public void shouldPutCarOnSale() throws Exception {
        CarOnSaleRequest carOnSaleRequest = constructPutCarOnSaleRequest();

        when(carService.putCarToSale(carOnSaleRequest)).thenReturn(1L);

        String advertId = mockMvc.perform(put("/car")
            .contentType(APPLICATION_JSON_UTF8_VALUE)
            .content(
                "{\"car\":{\"brand\":\"ford\",\"model\":\"fiesta\",\"plateNumber\":\"12-22\",\"color\":\"green\",\"year\":1212}," +
                    "\"client\":{\"name\":\"Andrey\",\"sureName\":\"Sevruk\",\"phoneNumber\":\"093\"}," +
                    "\"price\":1000.0}"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
            .andReturn()
            .getResponse()
            .getContentAsString();

        verify(carService).putCarToSale(carOnSaleRequest);

        assertThat(advertId).isEqualTo("1");
    }

    @Test
    public void shouldRejectDeal() throws Exception {
        mockMvc.perform(post("/rejectDeal")
            .param("dealId", "1")
            .contentType(APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk());

        verify(carService).rejectDeal(1L);
    }

    @Test
    public void chooseBestDeal() throws Exception {
        when(carService.chooseBestDealByAdvertId(1L)).thenReturn(2L);

        String advertId = mockMvc.perform(get("/bestDeal")
            .param("advertId", "1")
            .contentType(APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        verify(carService).chooseBestDealByAdvertId(1);

        assertThat(Long.valueOf(advertId)).isEqualTo(2L);

    }

    @Test
    public void shouldCreateDeal() throws Exception {
        DealRequest dealRequest = new DealRequest(new Client("Andrey", "Sevruk", "093"), 500.0);

        when(carService.createDeal(dealRequest, 1)).thenReturn(1L);

        String dealID = mockMvc.perform(post("/deal")
            .contentType(APPLICATION_JSON_UTF8_VALUE)
            .content("{\"client\":{\"name\":\"Andrey\",\"sureName\":\"Sevruk\",\"phoneNumber\":\"093\"},\"price\":500.0}")
            .param("advertId", "1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertThat(dealID).isEqualTo("1");
    }

    @Test
    public void shouldReturnExceptionIfDealIsNotFound() throws Exception {
        doThrow(new DealNotFoundException("Deal not found")).when(carService).rejectDeal(101010L);

        mockMvc.perform(post("/rejectDeal")
            .param("dealId", "101010")
            .contentType(APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status()
                .isNotFound());
    }

    @Test
    public void shouldReturnExceptionIfAdvertIsNotFound() throws Exception {
        doThrow(new DealNotFoundException("Advert not found"))
            .when(carService).putCarToSale(any(CarOnSaleRequest.class));

        mockMvc.perform(put("/car")
            .contentType(APPLICATION_JSON_UTF8_VALUE)
            .content(
                "{\"car\":{\"brand\":\"ford\",\"model\":\"fiesta\",\"plateNumber\":\"12-22\",\"color\":\"green\",\"year\":1212}," +
                    "\"client\":{\"name\":\"Andrey\",\"sureName\":\"Sevruk\",\"phoneNumber\":\"093\"}," +
                    "\"price\":1000.0}"))
            .andExpect(status().isNotFound());
    }

    private CarOnSaleRequest constructPutCarOnSaleRequest() {
        return CarOnSaleRequest
            .builder()
            .car(new Car("ford", "fiesta", "12-22", "green", 1212))
            .client(new Client("Andrey", "Sevruk", "093"))
            .price(1000)
            .build();
    }

    private Car constructCar() {
        return new Car("ford", "fiesta", "12-22", "green", 1212);
    }

    private CarSaleInfo constructCarsForSale() {
        return new CarSaleInfo(1, constructCar(), new SaleInfo("Andrey", 1000));
    }

}