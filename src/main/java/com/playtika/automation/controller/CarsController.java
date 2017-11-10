package com.playtika.automation.controller;

import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarForSale;
import com.playtika.automation.domain.CarInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Slf4j
@RequestMapping(produces = APPLICATION_JSON_UTF8_VALUE)
public class CarsController {

    private final AtomicLong generateId = new AtomicLong();
    private final Map<Long, CarForSale> cars = new ConcurrentHashMap<>();

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Id")
    public class ResourceNotFoundException extends RuntimeException {
        ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @PostMapping(value = "/car")
    public long addCar(@RequestBody Car car,
                       @RequestParam("price") double price,
                       @RequestParam("ownerContacts") String ownerContacts) {

        long carId = generateId.incrementAndGet();
        CarForSale carForSale = new CarForSale();

        carForSale.setId(carId);
        carForSale.setBrand(car.getBrand());
        carForSale.setModel(car.getModel());
        carForSale.setOwnerContacts(ownerContacts);
        carForSale.setPrice(price);

        cars.put(carId, carForSale);
        log.info("addCar was finished [carId: {}; carInfo: {}]", carId, car);

        return carId;
    }

    @GetMapping(value = "/cars")
    public Map<Long, CarForSale> getAllCars() {
        return cars;
    }

    @DeleteMapping(value = "/car")
    public void deleteCar(@RequestParam("id") long id) {
        cars.remove(id);
    }

    @GetMapping(value = "/car")
    public CarInfo getCarDetails(@RequestParam("id") long id) {
        if (cars.get(id) == null) {
            throw new ResourceNotFoundException("Car with id" + id + " not found");
        }

        CarInfo carInfo = new CarInfo();
        carInfo.setOwnerContacts(cars.get(id).getOwnerContacts());
        carInfo.setPrice(cars.get(id).getPrice());

        return carInfo;
    }
}
