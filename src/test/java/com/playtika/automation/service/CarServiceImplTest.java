package com.playtika.automation.service;

import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarSaleInfo;
import com.playtika.automation.domain.SaleInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
@RunWith(SpringRunner.class)
public class CarServiceImplTest {

    private double price = 1000.0;
    private String owner = "Andrey";
    private Long id = 1L;
    private CarService carService;
    @Autowired
    EntityManager manager;

    @Before
    public void init(){
        carService = new CarServiceImpl(manager);
    }

    @Test
    public void shouldReturnAllCars() {
        addCarToDB();

        List<CarSaleInfo> expected = new ArrayList<>();
        expected.add(getCarSaleInfo());

        assertThat(carService.getAllCars()).isEqualTo(expected);
    }

    @Test
    public void shouldNotAddCarIfTheSameAlreadyExist(){
        assertThat(false);
    }

    @Test
    public void shouldNotAddClientIfTheSameAlreadyExist(){
        assertThat(false);
    }

    @Test
    public void shouldNotCreateAdvertIfPriceZero(){
        assertThat(false);
    }


    @Test
    public void shouldReturnEmptyMapIfCollectionOfCarIsEmpty() {
        assertThat(carService.getAllCars()).isEmpty();
    }

    @Test
    public void shouldDeleteCarById() {
        addCarToDB();

        carService.deleteCar(1);

        assertThat(carService.getAllCars()).isEmpty();
    }

    @Test
    public void shouldGetCarSaleInfoById() {
        addCarsToCollection();

        Optional<SaleInfo> expected = Optional.of(new SaleInfo(owner, price));

        assertThat(carService.getSaleInfo(1)).isEqualTo(expected);
    }

    private void addCarToDB() {
        Car car = getCar();
        carService.addCar(car, price, owner);
    }

    private Car getCar() {
        return new Car("ford", "fiesta", "12-12", "green", 2016);
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
