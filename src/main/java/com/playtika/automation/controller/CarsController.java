package com.playtika.automation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CarsController {

    @PostMapping
    public long addCar(@RequestBody Car car) {
        return 1l;
    }

    @GetMapping("/getAllCars")
    public String getAllCars() {

        return "";
    }

    @DeleteMapping("/deleteCar")
    public void deleteCar(@RequestParam("id") long id) {

    }

    @GetMapping("/getCarInfo")
    public String getCarDetails(@RequestParam("id") long id) {
        return "";
    }
}
