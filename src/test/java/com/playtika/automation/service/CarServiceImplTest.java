package com.playtika.automation.service;

import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarSaleInfo;
import com.playtika.automation.domain.SaleInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceImplTest {

    private CarService carService = new CarServiceImpl();
    private double price = 1000.0;
    private String owner = "Andrey";
    private Long id = 1L;


    @Test
    public void shouldCorrectGenerateId() {
        carService.addCar(getCar(), price, owner);
        assertThat(carService.addCar(getCar(), price, owner)).isEqualTo(2);
    }

    @Test
    public void shouldReturnAllCars() {
        addCarToCollection();

        List<CarSaleInfo> expected = new ArrayList<>();
        expected.add(getCarSaleInfo());

        assertThat(carService.getAllCars()).isEqualTo(expected);
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
    public void shouldGetCarSaleInfoById() {
        addCarsToCollection();

        Optional<SaleInfo> expected = Optional.of(new SaleInfo(owner, price));

        assertThat(carService.getSaleInfo(1)).isEqualTo(expected);
    }

    private void addCarToCollection() {
        Car car = getCar();
        carService.addCar(car, price, owner);
    }

    private Car getCar() {
        return new Car("ford", "fiesta");
    }

    private void addCarsToCollection() {
        Car car = getCar();
        carService.addCar(car, price, owner);
        carService.addCar(car, price, owner);
    }

    private CarSaleInfo getCarSaleInfo() {
        return new CarSaleInfo(1, getCar(), new SaleInfo(owner, price));
    }
}
