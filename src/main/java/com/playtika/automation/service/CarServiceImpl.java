package com.playtika.automation.service;

import com.playtika.automation.web.CarsController;
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
        CarForSale carForSale = CarForSale
            .builder()
            .id(carId)
            .brand(car.getBrand())
            .model(car.getModel())
            .ownerContacts(ownerContacts)
            .price(price)
            .build();

        cars.put(carId, carForSale);
        return carId;
    }

    @Override
    public Map<Long, CarForSale> getAllCars() {
        return cars;
    }

    @Override
    public boolean deleteCar(long id) {

        try{
            cars.remove(id);
            return true;

        }catch (RuntimeException e){
            return false;
        }
    }

    @Override
    public CarInfo getCarDetails(long id) {
        if (cars.get(id) == null) {
            throw new CarsController.ResourceNotFoundException("Car with id" + id + " not found");
        }

        return CarInfo.builder()
            .ownerContacts(cars.get(id).getOwnerContacts())
            .price(cars.get(id).getPrice())
            .build();
    }
}
