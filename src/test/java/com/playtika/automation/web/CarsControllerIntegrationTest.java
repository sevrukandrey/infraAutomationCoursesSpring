package com.playtika.automation.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarForSale;
import com.playtika.automation.domain.CarInfo;
import com.playtika.automation.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
        cars.put(2L, getCarForSale());

        when(carService.getAllCars()).thenReturn(cars);

        mockMvc.perform(get("/cars")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.2.id", is(2)))
            .andExpect(jsonPath("$.2.model", is("x5")))
            .andExpect(jsonPath("$.2.brand", is("bmw")))
            .andExpect(jsonPath("$.2.price", is(1000.0)))
            .andExpect(jsonPath("$.2.ownerContacts", is("Andrey")));

        verify(carService, times(1)).getAllCars();
    }


    @Test
    public void shouldInvokeDeleteCar() throws Exception {
        doNothing().when(carService).deleteCar(anyLong());

        mockMvc.perform(delete("/car")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .param("id", String.valueOf(1L)))
            .andExpect(status().isOk());

        verify(carService, times(1)).deleteCar(anyLong());
    }

    @Test
    public void shouldReturnCarDetails() throws Exception {
        CarInfo carInfo = getCarInfo();

        when(carService.getCarDetails(anyLong())).thenReturn(carInfo);

        mockMvc.perform(get("/car")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .param("id", String.valueOf(1L)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.ownerContacts", is("Andrey")))
            .andExpect(jsonPath("$.price", is(1000.0)));

        verify(carService, times(1)).getCarDetails(1);
    }

    @Test
    public void ifCarIdParamIsMissedThrowException() throws Exception {
        mockMvc.perform(get("/car").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    private Car getCar() {
        return Car
            .builder()
            .brand("bmw")
            .model("x5")
            .build();
    }

    private CarForSale getCarForSale() {
        return CarForSale.builder()
            .id(2)
            .model("x5")
            .brand("bmw")
            .ownerContacts("Andrey")
            .price(1000)
            .build();
    }

    private CarInfo getCarInfo() {
        CarInfo carInfo = new CarInfo();
        carInfo.setPrice(1000);
        carInfo.setOwnerContacts("Andrey");
        return carInfo;
    }
}