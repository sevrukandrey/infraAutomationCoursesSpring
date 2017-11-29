package com.playtika.automation.web;

import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarSaleInfo;
import com.playtika.automation.domain.SaleInfo;
import com.playtika.automation.service.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Slf4j
@RequestMapping(produces = APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class CarsController {

    private final CarService carService;

    @ResponseStatus(value = NOT_FOUND)
    public static class ResourceNotFoundException extends NullPointerException {
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
    public List<CarSaleInfo> getAllCars() {
        return carService.getAllCars();
    }

    @DeleteMapping(value = "/cars/{id}")
    public void deleteCar(@PathVariable("id") long id) {
        carService.deleteCar(id);
    }

    @GetMapping(value = "/cars/{id}")
    public SaleInfo getCarDetails(@PathVariable("id") long id) {
        try{
            return carService.getSaleInfo(id).orElseThrow(NullPointerException::new);
        }catch (NullPointerException e){
            throw new ResourceNotFoundException("There is no Sale Info for car with id  " + id);
        }
    }
}
