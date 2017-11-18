package com.playtika.automation.service;

import com.playtika.automation.web.CarsController;
import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarSaleInfo;
import com.playtika.automation.domain.SaleInfo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CarServiceImpl implements CarService {

    private final AtomicLong generateId = new AtomicLong();
    private final Map<Long, CarSaleInfo> cars = new ConcurrentHashMap<>();

    @Override
    public long addCar(Car car, double price, String ownerContacts) {
        long carId = generateId.incrementAndGet();

        CarSaleInfo carForSale =new CarSaleInfo(carId, car, new SaleInfo(ownerContacts, price));

        cars.put(carId, carForSale);

        return carId;
    }

    @Override
    public List<CarSaleInfo> getAllCars() {
    return new ArrayList<>(cars.values());
    }

    @Override
    public void deleteCar(long id) {
           cars.remove(id);
    }

    @Override
    public Optional<SaleInfo> getSaleInfo(long id) {

        return Optional.of(cars.get(id).getSaleInfo());
    }
}
