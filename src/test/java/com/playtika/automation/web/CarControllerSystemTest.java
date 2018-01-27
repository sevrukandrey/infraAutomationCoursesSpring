package com.playtika.automation.web;

import com.playtika.automation.domain.Car;
import com.playtika.automation.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.Long.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class CarControllerSystemTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Test
    public void shouldAddCarAndReturnId() throws Exception {

        when(carService.addCar(Matchers.any(Car.class), anyDouble(), anyString())).thenReturn(123L);
        Long carId = postCar();

        assertThat(carId).isGreaterThan(123);
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
    public void shouldGetSaleInfoByCarId() throws Exception {
        long carId = postCar();

        mockMvc.perform(get("/cars/" + carId)
                                .contentType(APPLICATION_JSON_UTF8_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.ownerContacts", is("Andrey")))
               .andExpect(jsonPath("$.price", is(1000.0)));
    }

    @Test
    public void shouldDeleteCarById() throws Exception {
        Long carId = postCar();

        mockMvc.perform(delete("/cars/" + carId)
                                .contentType(APPLICATION_JSON_UTF8_VALUE))
               .andExpect(status().isOk());

        mockMvc.perform(get("/cars/" + carId)
                                .contentType(APPLICATION_JSON_UTF8_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
    public void shouldPutCarOnSale() throws Exception {
        String putCarForSale = "{\"car\":{\"brand\":\"ford\",\"model\":\"fiesta\",\"plateNumber\":\"12-22\",\"color\":\"green\",\"year\":1212}," +
                "\"client\":{\"name\":\"Andrey\",\"sureName\":\"Sevruk\",\"phoneNumber\":\"093\"}," +
                "\"price\":1000.0}";

        String advertIdResponse = mockMvc.perform(put("/car")
                                                          .contentType(APPLICATION_JSON_UTF8_VALUE)
                                                          .content(putCarForSale))
                                         .andExpect(status().isOk())
                                         .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                                         .andReturn()
                                         .getResponse()
                                         .getContentAsString();

        mockMvc.perform(get("/carByAdvertId")
                                .contentType(APPLICATION_JSON_UTF8_VALUE)
                                .param("advertId", advertIdResponse))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.year", is(1212)))
               .andExpect(jsonPath("$.color", is("green")))
               .andExpect(jsonPath("$.plateNumber", is("12-22")))
               .andExpect(jsonPath("$.model", is("fiesta")))
               .andExpect(jsonPath("$.brand", is("ford")));
    }

    @Test
    public void shouldChooseBestDeal() throws Exception {
        long advertId = createAdvert();
        long higherPriceDealId = createDealByAdvertIdWithPrice(advertId, 500);
        createDealByAdvertIdWithPrice(advertId, 100);

        String bestDealId = mockMvc.perform(get("/bestDeal")
                                                    .contentType(APPLICATION_JSON_UTF8_VALUE)
                                                    .param("advertId", String.valueOf(advertId)))
                                   .andExpect(status().isOk())
                                   .andReturn()
                                   .getResponse()
                                   .getContentAsString();

        assertThat(Long.valueOf(bestDealId)).isEqualTo(higherPriceDealId);
    }

    @Test
    public void shouldRejectDealById() throws Exception {
        long advertId = createAdvert();
        long dealId = createDealByAdvertIdWithPrice(advertId, 500);

        mockMvc.perform(post("/rejectDeal")
                                .contentType(APPLICATION_JSON_UTF8_VALUE)
                                .param("dealId", String.valueOf(dealId)))
               .andDo(print())
               .andExpect(status().isOk());

        mockMvc.perform(get("/dealById")
                                .contentType(APPLICATION_JSON_UTF8_VALUE)
                                .param("dealId", String.valueOf(dealId)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status", is("REJECTED")));
    }

    @Test
    public void shouldCreateDeal() throws Exception {
        long advertId = createAdvert();

        String dealId = mockMvc.perform(post("/deal")
                                                .contentType(APPLICATION_JSON_UTF8_VALUE)
                                                .content("{\"client\":{\"name\":\"Andrey\",\"sureName\":\"Sevruk\",\"phoneNumber\":\"093\"},\"price\":500.0}")
                                                .param("advertId", String.valueOf(advertId)))
                               .andExpect(status().isOk())
                               .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                               .andReturn()
                               .getResponse()
                               .getContentAsString();

        mockMvc.perform(get("/dealById")
                                .contentType(APPLICATION_JSON_UTF8_VALUE)
                                .param("dealId", dealId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    @Test
    public void shouldReturnAdvertIdByCarId() throws Exception {
        long id = postCar();

        String advertId = mockMvc.perform(get("/advertByCarId")
                                                  .contentType(APPLICATION_JSON_UTF8_VALUE)
                                                  .param("carId", String.valueOf(id)))
                                 .andExpect(status().isOk())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

        mockMvc.perform(get("/carByAdvertId")
                                .contentType(APPLICATION_JSON_UTF8_VALUE)
                                .param("advertId", advertId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.model", is("fiesta")))
               .andExpect(jsonPath("$.brand", is("Ford")));
    }


    private long createDealByAdvertIdWithPrice(long advertId, double price) throws Exception {
        String id = mockMvc.perform(post("/deal")
                                            .contentType(APPLICATION_JSON_UTF8_VALUE)
                                            .content("{\"client\":{\"name\":\"Andrey\",\"sureName\":\"Sevruk\",\"phoneNumber\":\"093\"},\"price\":" + price + "}")
                                            .param("advertId", String.valueOf(advertId)))
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                           .andReturn()
                           .getResponse()
                           .getContentAsString();
        return Long.valueOf(id);
    }

    private long createAdvert() throws Exception {
        String putCarForSale = "{\"car\":{\"brand\":\"ford\",\"model\":\"fiesta\",\"plateNumber\":\"12-22\",\"color\":\"green\",\"year\":1212}," +
                "\"client\":{\"name\":\"Andrey\",\"sureName\":\"Sevruk\",\"phoneNumber\":\"093\"}," +
                "\"price\":1000.0}";

        String advertId = mockMvc.perform(put("/car")
                                                  .contentType(APPLICATION_JSON_UTF8_VALUE)
                                                  .content(putCarForSale))
                                 .andExpect(status().isOk())
                                 .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();
        return (Long.valueOf(advertId));
    }

    private long postCar() throws Exception {
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
        return valueOf(id);
    }

    @EnableAutoConfiguration
    public static class TestConfiguration {
    }
}
