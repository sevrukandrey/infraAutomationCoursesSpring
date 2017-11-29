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
import java.util.List;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class CarServiceImplTest {

    private double price = 1000.0;
    private String owner = "Andrey";
    private Car car;
    private SaleInfo saleInfo;

    private CarService carService;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void init() {
        carService = new CarServiceImpl(entityManager);

        car = new Car("ford", "fiesta", "12-12", "green", 2016);
        saleInfo = new SaleInfo(owner, price);
    }

    @Test
    public void shouldReturnAllCars() {
        addCarToDB();

        List<CarSaleInfo> allCars = carService.getAllCars();

        assertThat(allCars).hasSize(1);
        CarSaleInfo carSaleInfo = allCars.get(0);
        assertThat(carSaleInfo.getCar()).isEqualTo(car);
        assertThat(carSaleInfo.getSaleInfo()).isEqualTo(saleInfo);
    }

    @Test
    public void shouldReturnEmptyListIfThereAreNoCarsInDB() {
        assertThat(carService.getAllCars())
            .isEmpty();
    }

    @Test
    public void shouldDeleteCarById() {
        long carId = addCarToDB();

        carService.deleteCar(carId);

        assertThat(carService.getAllCars()).isEmpty();
    }

    @Test
    public void shouldGetCarSaleInfoById() {
        long carId = addCarToDB();

        assertThat(carService.getSaleInfo(carId))
            .isEqualTo(of(new SaleInfo(owner, price)));
    }

    private long addCarToDB() {
        return carService.addCar(car, price, owner);
    }

}
