package com.playtika.automation.controller;

import com.playtika.automation.domain.Car;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Controller
public class CarsController {

    @PostMapping(value = "/addCar")
    @ResponseBody
    public long addCar(@RequestBody Car car) {
        return 1L;
    }

    @GetMapping(value = "/getAllCars")
    @ResponseBody
    public String getAllCars() {

        return "123";
    }

    @DeleteMapping(value = "/deleteCar")
    @ResponseBody
    public void deleteCar(@RequestParam("id") long id) {

    }

    @GetMapping(value = "/getCarInfo")
    @ResponseBody
    public String getCarDetails(@RequestParam("id") long id) {
        return "123123";
    }
}
