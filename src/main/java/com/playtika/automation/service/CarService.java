package com.playtika.automation.service;

import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarOnSaleRequest;
import com.playtika.automation.domain.CarSaleInfo;
import com.playtika.automation.domain.SaleInfo;

import java.util.List;
import java.util.Optional;

public interface CarService {

    long addCar(Car car, double price, String ownerContacts);

    List<CarSaleInfo> getAllCars();

    void deleteCar(long id);

    Optional<SaleInfo> getSaleInfo(long id);

    void rejectDeal(Long id);

    long putCarToSale(CarOnSaleRequest carOnSaleRequest);

    long chooseBestDealByAdvertId(long advertId);
}
