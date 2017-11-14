package com.playtika.automation.service;

import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarForSale;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceImplTest {

    private CarService carService;
    private double price = 1000.0;
    private String owner = "Andrey";

    @Before
    public void init() {
        carService = new CarServiceImpl();
    }

    @Test
    public void shouldReturnAllCars() {
        Car car = getCar();
        carService.addCar(car, price, owner);

        Map<Long, CarForSale> expected = new HashMap<>();
        expected.put(1L,getCarForSale());

        assertThat(carService.getAllCars()).isEqualTo(expected);
    }

    @Test
    public void shouldReturnCorectSizeForGetAllCars() {

        Car car = getCar();
        carService.addCar(car, price, owner);
        carService.addCar(car, price, owner);

        assertThat(carService.getAllCars()).hasSize(2);
    }


    private Car getCar() {
        return Car.builder()
            .brand("ford")
            .model("fiesta")
            .build();
    }

    private CarForSale getCarForSale() {
        CarForSale carForSale  = new CarForSale();
        carForSale.setId(1);
        carForSale.setOwnerContacts("Andrey");
        carForSale.setBrand("ford");
        carForSale.setModel("fiesta");
        carForSale.setPrice(1000);
        return carForSale;

    }
}
