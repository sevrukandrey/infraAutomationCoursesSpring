package com.playtika.automation.controller;

import com.playtika.automation.controller.dto.CarForSale;
import com.playtika.automation.domain.Car;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.http.MediaType.*;

@RestController
@Slf4j
@RequestMapping(produces = APPLICATION_JSON_UTF8_VALUE)
public class CarsController {
    private AtomicLong generateId = new AtomicLong();
    private Map<Long, CarForSale> cars = new ConcurrentHashMap<>();

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Id")
    public class ResourceNotFoundException extends RuntimeException {
            public  ResourceNotFoundException(String message){
                super(message);
            }
    }



    @PostMapping(value = "/car")
    public CarForSale addCar(@RequestBody Car car,
                       @PathVariable("price") double price,
                       @PathVariable("ownerContacts") String ownerContacts) {

        long carId = generateId.getAndIncrement();
        CarForSale carForSale = new CarForSale();

        carForSale.setId(carId);
        carForSale.setBrand(car.getBrand());
        carForSale.setModel(car.getModel());
        carForSale.setOwnerContacts(ownerContacts);
        carForSale.setPrice(price);


        cars.put(carId, carForSale);

        log.info("addCar was finished [carId: {}; carInfo: {}]", carId, car);

        return carForSale;
    }

    @GetMapping(value = "/cars")
    public Map<Long, CarForSale> getAllCars() {
        return cars;
    }

    @DeleteMapping(value = "/car")
    public void deleteCar(@RequestParam("id") long id) {
        cars.remove(id);
    }

    @GetMapping(value = "/carInfo/{id}")
    public String getCarDetails(@PathVariable("id") long id) {
        if (cars.get(id) == null) {
          throw new ResourceNotFoundException("Car with id" + id + " not found");
        }
        return cars.get(id).getOwnerContacts() + " " + cars.get(id).getPrice();
    }
}
