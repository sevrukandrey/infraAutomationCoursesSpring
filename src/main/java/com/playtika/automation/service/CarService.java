package com.playtika.automation.service;

import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarSaleInfo;
import com.playtika.automation.domain.SaleInfo;

import java.util.Collection;

public interface CarService {

    long addCar(Car car, double price, String ownerContacts);

     Collection<CarSaleInfo> getAllCars();

    boolean deleteCar(long id);

    SaleInfo getSaleInfo(long id);
    
}
