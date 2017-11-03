package com.playtika.automation.controller;

import com.playtika.automation.domain.Car;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(produces = APPLICATION_JSON_UTF8_VALUE)
public class CarsController {
    List<Car> cars = new ArrayList<>();

    @PostMapping(value = "/addCar")
    @ResponseBody
    public ResponseEntity addCar(@RequestBody Car car) throws JSONException {
        JSONObject response = new JSONObject();
        response.put("carId", "100500");

        return new ResponseEntity(response.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/getAllCars")
    public List<Car> getAllCars() {
        return cars;
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
