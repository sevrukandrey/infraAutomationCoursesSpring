package com.playtika.automation.controller;

import com.playtika.automation.domain.Car;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.http.MediaType.*;

@Controller
@Slf4j
@RequestMapping(produces = APPLICATION_JSON_UTF8_VALUE)
public class CarsController {

    private Map<Long, Car> cars = new HashMap<>();

    @PostMapping(value = "/addCar")
    @ResponseBody
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

    @GetMapping(value = "/getAllCars")
    @ResponseBody
    public Map<Long, Car> getAllCars() {
        return cars;
    }

    @DeleteMapping(value = "/deleteCar")
    @ResponseBody
    public void deleteCar(@RequestParam("id") long id) {
        cars.remove(id);
    }

    @GetMapping(value = "/getCarInfo")
    @ResponseBody
    public String getCarDetails(@RequestParam("id") long id) {
        return cars.get(id).getOwnerContacts() + " " + cars.get(id).getPrice();
    }
}
