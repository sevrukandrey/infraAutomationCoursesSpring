package com.playtika.automation.web;

import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarForSale;
import com.playtika.automation.domain.CarInfo;
import com.playtika.automation.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Slf4j
@RequestMapping(produces = APPLICATION_JSON_UTF8_VALUE)
public class CarsController {
    private final CarService carService;

    @Autowired
    public CarsController(CarService carService) {
        this.carService = carService;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Id")
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @PostMapping(value = "/cars")
    public long addCar(@RequestBody Car car,
                       @RequestParam("price") double price,
                       @RequestParam("ownerContacts") String ownerContacts) {

        long id = carService.addCar(car, price, ownerContacts);

        log.info("addCar was finished [carId: {}; carInfo: {}]", id, car);

        return id;
    }

    @GetMapping(value = "/cars")
    public Map<Long, CarForSale> getAllCars() {
        return carService.getAllCars();
    }

    @DeleteMapping(value = "/cars{id}")
    public void deleteCar(@PathVariable("id") long id) {
        carService.deleteCar(id);
    }

    @GetMapping(value = "/cars{id}")
    public CarInfo getCarDetails(@PathVariable("id") long id) {
        return carService.getCarDetails(id);
    }
}
