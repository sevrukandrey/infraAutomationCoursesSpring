package com.playtika.automation.controller;

import com.playtika.automation.domain.Car;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Controller
public class CarsController {
    List<Car> cars = new ArrayList<>();


    @PostMapping(value = "/addCar")
    @ResponseBody
    public long addCar(@RequestBody Car car) {
        cars.add(car);
        return car.id;
    }

    @GetMapping(value = "/getAllCars")
    @ResponseBody
    public String getAllCars() {
        return cars.toString();
    }

    @DeleteMapping(value = "/deleteCar")
    @ResponseBody
    public void deleteCar(@RequestParam("id") long id) {
            cars.stream()
    }

    @GetMapping(value = "/getCarInfo")
    @ResponseBody
    public String getCarDetails(@RequestParam("id") long id) {
        return "123123";
    }
}
