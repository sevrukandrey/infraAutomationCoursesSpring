package com.playtika.automation.service;

import com.playtika.automation.controller.CarsController;
import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarForSale;
import com.playtika.automation.domain.CarInfo;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CarServiceImpl implements CarService {

    private final AtomicLong generateId = new AtomicLong();
    private final Map<Long, CarForSale> cars = new ConcurrentHashMap<>();

    @Override
    public long addCar(Car car, double price, String ownerContacts) {
        long carId = generateId.incrementAndGet();
        CarForSale carForSale = new CarForSale();

        carForSale.setId(carId);
        carForSale.setBrand(car.getBrand());
        carForSale.setModel(car.getModel());
        carForSale.setOwnerContacts(ownerContacts);
        carForSale.setPrice(price);

        cars.put(carId, carForSale);
        return carId;
    }

    @Override
    public Map<Long, CarForSale> getAllCars() {
        return cars;
    }

    @Override
    public void deleteCar(long id) {
        cars.remove(id);
    }

    @Override
    public CarInfo getCarDetails(long id) {
        if (cars.get(id) == null) {
            throw new CarsController.ResourceNotFoundException("Car with id" + id + " not found");
        }

        CarInfo carInfo = new CarInfo();
        carInfo.setOwnerContacts(cars.get(id).getOwnerContacts());
        carInfo.setPrice(cars.get(id).getPrice());

        return carInfo;
    }
}
