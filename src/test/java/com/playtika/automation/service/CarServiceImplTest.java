package com.playtika.automation.service;

import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarForSale;
import com.playtika.automation.domain.CarInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceImplTest {

    private CarService carService = new CarServiceImpl();
    private double price = 1000.0;
    private String owner = "Andrey";
    private Long id = 1L;


    @Test
    public void shouldCorrectGenerateId() {

        assertThat(carService.addCar(getCar(), price, owner)).isEqualTo(1);
    }

    @Test
    public void shouldGenerateNewId() {
        carService.addCar(getCar(), price, owner);

        assertThat(carService.addCar(getCar(), price, owner)).isEqualTo(2);
    }

    @Test
    public void shouldSaveCarWithParametersToCollection() {
        Car car = getCar();
        Map<Long, CarForSale> cars = new ConcurrentHashMap<>();
        cars.put(id, getCarForSale());

        carService.addCar(car, price, owner);

        assertThat(carService.getAllCars()).isEqualTo(cars);
    }

    @Test
    public void shouldHasCorrectSizeOfCollection() {
        Car car = getCar();
        Map<Long, CarForSale> cars = new ConcurrentHashMap<>();
        cars.put(id, getCarForSale());
        cars.put(id + 1, getCarForSale());


        carService.addCar(car, price, owner);
        carService.addCar(car, price, owner);

        assertThat(carService.getAllCars().size()).isEqualTo(cars.size());
    }

    @Test
    public void shouldReturnAllCars() {
        addCarToCollection();

        Map<Long, CarForSale> expected = new HashMap<>();
        expected.put(1L, getCarForSale());

        assertThat(carService.getAllCars()).isEqualTo(expected);
    }

    @Test
    public void shouldReturnCorrectSizeForGetAllCars() {
        addCarsToCollection();

        assertThat(carService.getAllCars()).hasSize(2);
    }

    @Test
    public void shouldReturnEmptyMapIfCollectionOfCarIsEmpty() {
        assertThat(carService.getAllCars()).isEmpty();
    }


    @Test
    public void shouldDeleteCarById() {
        addCarToCollection();

        carService.deleteCar(1);

        assertThat(carService.getAllCars()).isEmpty();
    }

    @Test
    public void shouldNotThrowExceptionIfCarByIdNotFound() {
        addCarToCollection();

        assertThatCode(() -> {
            carService.deleteCar(1);
        }).doesNotThrowAnyException();

    }

    @Test
    public void shouldGetCarDetailsById() {
        addCarsToCollection();

        CarInfo expected = getCarInfo(owner, price);

        assertThat(carService.getCarDetails(1)).isEqualTo(expected);
    }

    private CarInfo getCarInfo(String owner, double price) {
        return CarInfo.builder()
            .ownerContacts(owner)
            .price(price)
            .build();
    }

    private void addCarToCollection() {
        Car car = getCar();
        carService.addCar(car, price, owner);
    }

    private Car getCar() {
        return Car.builder()
            .brand("ford")
            .model("fiesta")
            .build();
    }

    private CarForSale getCarForSale() {
        return CarForSale.builder().
            id(1)
            .ownerContacts(owner)
            .brand("ford")
            .model("fiesta")
            .price(price)
            .build();
    }

    private void addCarsToCollection() {
        Car car = getCar();
        carService.addCar(car, price, owner);
        carService.addCar(car, price, owner);
    }

}
