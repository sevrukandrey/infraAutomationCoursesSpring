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

import static java.lang.Long.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        Long carId = postCar();

        assertThat(carId).isGreaterThan(0);

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
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.ownerContacts", is("Andrey")))
                .andExpect(jsonPath("$.price", is(1000.0)))
                .andReturn()
                .getResponse()
                .getContentAsString();
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
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
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
                .param("dealId", String.valueOf(dealId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.id", is(String.valueOf(dealId))));

    }

    @Test
    public void shouldReturnAdvertIdByCarId() throws Exception {
        Long id = postCar();

        String advertId = mockMvc.perform(get("/advertByCarId")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .param("carId", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(Long.valueOf(advertId)).isGreaterThan(0);

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
        return valueOf(id);
    }
}
