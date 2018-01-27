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

import static java.lang.Long.valueOf;
import static java.util.Collections.emptyList;
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

    private double price = 1000.0;
    private Client client = new Client("Andrey", "Sevruk", "093");
    private String ownerContacts = "Andrey";
    private long carId = 1L;
    private long advertId = 2L;
    private long dealId = 3L;

    @Test
    public void shouldAddCar() throws Exception {
        when(carService.addCar(constructCar(), price, ownerContacts)).thenReturn(carId);

        String carIdResponse = mockMvc.perform(post("/cars")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content("{\"brand\": \"ford\",\"model\":\"fiesta\",\"plateNumber\":\"12-22\",\"year\":\"1212\",\"color\":\"green\"}")
                .param("price", String.valueOf(price))
                .param("ownerContacts", ownerContacts))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(valueOf(carIdResponse)).isEqualTo(carId);
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
        when(carService.getAllCars()).thenReturn(emptyList());

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE));

        verify(carService).getAllCars();
    }

    @Test
    public void shouldDeleteCar() throws Exception {
        doNothing().when(carService).deleteCar(carId);

        mockMvc.perform(delete("/cars/" + carId)
                .contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());

        verify(carService).deleteCar(carId);
    }

    @Test
    public void shouldReturnCarSaleInfo() throws Exception {
        SaleInfo carInfo = new SaleInfo(ownerContacts, price);

        when(carService.getSaleInfo(carId)).thenReturn(of(carInfo));

        mockMvc.perform(get("/cars/" + carId)
                .contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.ownerContacts", is(ownerContacts)))
                .andExpect(jsonPath("$.price", is(price)));

        verify(carService).getSaleInfo(carId);
    }

    @Test
    public void shouldReturnNotFoundIfCarNotFound() throws Exception {
        when(carService.getSaleInfo(carId)).thenReturn(null);

        mockMvc.perform(get("/cars/" + carId)
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldPutCarOnSale() throws Exception {
        CarOnSaleRequest carOnSaleRequest = constructPutCarOnSaleRequest();

        when(carService.putCarToSale(carOnSaleRequest)).thenReturn(advertId);

        String advertIdResponse = mockMvc.perform(put("/car")
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

        assertThat(valueOf(advertIdResponse)).isEqualTo(advertId);
    }

    @Test
    public void shouldRejectDeal() throws Exception {
        doNothing().when(carService).rejectDeal(dealId);

        mockMvc.perform(post("/rejectDeal")
                .param("dealId", String.valueOf(dealId))
                .contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());

        verify(carService).rejectDeal(dealId);
    }

    @Test
    public void chooseBestDeal() throws Exception {
        when(carService.chooseBestDealByAdvertId(advertId)).thenReturn(dealId);

        String dealIdResponse = mockMvc.perform(get("/bestDeal")
                .param("advertId", String.valueOf(advertId))
                .contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertThat(valueOf(dealIdResponse)).isEqualTo(dealId);
    }

    @Test
    public void shouldCreateDeal() throws Exception {
        DealRequest dealRequest = new DealRequest(new Client("Andrey", "Sevruk", "093"), 500.0);

        when(carService.createDeal(dealRequest, advertId)).thenReturn(dealId);

        String dealIdResponse = mockMvc.perform(post("/deal")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content("{\"client\":{\"name\":\"Andrey\",\"sureName\":\"Sevruk\",\"phoneNumber\":\"093\"},\"price\":500.0}")
                .param("advertId", String.valueOf(advertId)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(Long.valueOf(dealIdResponse)).isEqualTo(dealId);
    }

    @Test
    public void shouldReturn404IfDealIsNotFound() throws Exception {
        doThrow(new DealNotFoundException("Deal not found"))
                .when(carService).rejectDeal(dealId);

        mockMvc.perform(post("/rejectDeal")
                .param("dealId", String.valueOf(dealId))
                .contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    public void shouldReturn404IfAdvertIsNotFound() throws Exception {
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

    @Test
    public void shouldReturnAdvertIdByCarId() throws Exception {
        when(carService.getAdvertIdByCarId(carId)).thenReturn(advertId);

        String advertIdResult = mockMvc.perform(get("/advertByCarId")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .param("carId", String.valueOf(carId)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(valueOf(advertIdResult)).isEqualTo(advertId);
    }

    @Test
    public void shouldReturnCarByAdvertId() throws Exception {
        when(carService.getCarIdByAdvertId(advertId)).thenReturn(constructCar());

        mockMvc.perform(get("/carByAdvertId")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .param("advertId", String.valueOf(this.advertId)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.year", is(1212)))
                .andExpect(jsonPath("$.color", is("green")))
                .andExpect(jsonPath("$.plateNumber", is("12-22")))
                .andExpect(jsonPath("$.model", is("fiesta")))
                .andExpect(jsonPath("$.brand", is("ford")));
    }

    @Test
    public void shouldReturnDealById() throws Exception {
        when(carService.getDealById(dealId)).thenReturn(constructDeal());

        mockMvc.perform(get("/dealById")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .param("dealId", String.valueOf(dealId)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.price", is(500.0)))
                .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    private Deal constructDeal() {
        return new Deal(3L, client, 500.0, null, DealStatus.ACTIVE);
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