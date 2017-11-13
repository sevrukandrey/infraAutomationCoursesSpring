package com.playtika.automation.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtika.automation.domain.Car;
import com.playtika.automation.service.CarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
    public void name() throws Exception {
        when(carService.addCar(anyObject(),anyDouble(),anyString())).thenReturn(1L);

        mockMvc.perform(post("/car")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content("{\"brand\": \"Ford\",\"model\":fiesta}")
            .param("price", String.valueOf(1000))
            .param("ownerContacts", "Amdre"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"));
   }

    @Test
    public void name2() throws Exception {
        mockMvc.perform(post("car").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }


}
