package com.playtika.automation.controller;

import com.playtika.automation.domain.Car;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(produces = APPLICATION_JSON_UTF8_VALUE)
public class CarsController {
    //List<Car> cars = new ArrayList<>();

    Map<Long, Car> cars = new HashMap<>();

    @PostMapping(value = "/addCar")
    @ResponseBody
    public long addCar(@RequestBody Car car) {
        cars.put(car.id, car);
        return car.id;
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
    public Car getCarDetails(@RequestParam("id") long id) {
        return cars.get(id);
    }
}
