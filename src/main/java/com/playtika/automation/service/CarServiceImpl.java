package com.playtika.automation.service;

import com.playtika.automation.web.CarsController;
import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarSaleInfo;
import com.playtika.automation.domain.SaleInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
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


        System.out.println(cars.toString());
        return carId;
    }

    @Override
    public Collection<CarSaleInfo> getAllCars() {
        return cars.values();
    }

    @Override
    public boolean deleteCar(long id) {

        if (cars.get(id) == null) {
            return false;
        } else {
            cars.remove(id);
        }
        return true;
    }

    @Override
    public SaleInfo getSaleInfo(long id) {
        if (cars.get(id) == null) {
            System.out.println("!!!!!!!!!!!!!!");
        }
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        return cars.get(id).getSaleInfo();
    }
}
