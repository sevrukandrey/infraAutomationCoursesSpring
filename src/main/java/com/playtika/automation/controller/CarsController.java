package com.playtika.automation.controller;

import com.playtika.automation.domain.Car;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

import static org.springframework.http.MediaType.*;

@RestController
@Slf4j
@RequestMapping(produces = APPLICATION_JSON_UTF8_VALUE)
public class CarsController {

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Id")
    public class ResourceNotFoundException extends RuntimeException {
            public  ResourceNotFoundException(String message){
                super(message);
            }
    }

    private Map<Long, Car> cars = new HashMap<>();

    @PostMapping(value = "/car")
    public long addCar(@RequestBody Car car,
                       @RequestParam("price") double price,
                       @RequestParam("ownerContacts") String ownerContacts) {

        long carId = new Timestamp(System.currentTimeMillis()).getTime();

        car.setPrice(price);
        car.setOwnerContacts(ownerContacts);
        car.setId(carId);

        cars.put(carId, car);

        log.info("addCar was finished [carId: {}; carInfo: {}]", carId, car);

        return carId;
    }

    @GetMapping(value = "/cars")
    public Map<Long, Car> getAllCars() {
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
