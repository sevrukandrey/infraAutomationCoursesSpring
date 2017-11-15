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

import static org.assertj.core.api.Assertions.assertThat;

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
    public void shouldCorrectGenerateId(){

        assertThat(carService.addCar(getCar(),price,owner)).isEqualTo(1);
    }

    @Test
    public void shouldGenerateNewId(){
        carService.addCar(getCar(),price,owner);

        assertThat(carService.addCar(getCar(),price,owner)).isEqualTo(2);
    }












    //----------------------------------------------------------------------------------------------//

    @Test
    public void shouldReturnAllCars() {
        addCarToCollection();

        Map<Long, CarForSale> expected = new HashMap<>();
        expected.put(1L,getCarForSale());

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


    //----------------------------------------------------------------------------------------------//

    @Test
    public void shouldDeleteCarById() {
        addCarToCollection();
        carService.deleteCar(1);
        assertThat(carService.getAllCars()).isEmpty();

    }

    @Test
    public void shouldNotThrowExceptionIfCarByIdNotFound() {
        addCarToCollection();
        carService.deleteCar(1);

    }



    //----------------------------------------------------------------------------------------------//


    @Test
    public void shouldGetCarDetailsById(){
        addCarsToCollection();
        CarInfo expected = new CarInfo();
        expected.setOwnerContacts(owner);
        expected.setPrice(price);
        assertThat(carService.getCarDetails(1)).isEqualTo(expected);
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
        CarForSale carForSale  = new CarForSale();
        carForSale.setId(1);
        carForSale.setOwnerContacts("Andrey");
        carForSale.setBrand("ford");
        carForSale.setModel("fiesta");
        carForSale.setPrice(1000);
        return carForSale;

    }

    private void addCarsToCollection() {
        Car car = getCar();
        carService.addCar(car, price, owner);
        carService.addCar(car, price, owner);
    }

}
