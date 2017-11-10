package com.playtika.automation.service;

import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarForSale;
import com.playtika.automation.domain.CarInfo;

import java.util.Map;

public interface CarService {

    long addCar(Car car, double price, String ownerContacts);

    Map<Long, CarForSale> getAllCars();

    void deleteCar(long id);

    CarInfo getCarDetails(long id);


}
