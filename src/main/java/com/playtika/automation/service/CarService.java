package com.playtika.automation.service;

import com.playtika.automation.domain.*;

import java.util.List;
import java.util.Optional;

public interface CarService {

    long addCar(Car car, double price, String ownerContacts);

    List<CarSaleInfo> getAllCars();

    void deleteCar(long id);

    Optional<SaleInfo> getSaleInfo(long id);

    void rejectDeal(long dealId);

    long putCarToSale(CarOnSaleRequest carOnSaleRequest);

    long chooseBestDealByAdvertId(long advertId);

    long createDeal(DealRequest dealRequest, long advertId);
}
