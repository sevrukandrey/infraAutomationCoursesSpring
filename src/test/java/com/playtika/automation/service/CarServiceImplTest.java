package com.playtika.automation.service;

import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarForSale;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceImplTest {

    private CarService carService;

    @Before
    public void init() {
        carService = new CarServiceImpl();
    }

    @Test
    public void shouldReturnAllCars() {

    }

    private CarForSale getCarForSale() {
        CarForSale carForSale  = new CarForSale();
        carForSale.setId(1);
        carForSale.setOwnerContacts("Andrey");
        carForSale.setBrand("ford");
        carForSale.setModel("fiesta");
        carForSale.setPrice(100);

    }
}
