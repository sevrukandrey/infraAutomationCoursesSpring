package com.playtika.automation.web;

import com.playtika.automation.domain.*;
import com.playtika.automation.service.CarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Slf4j
@RequestMapping(produces = APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
@Api(value = "onlinestore", description = "Operations pertaining to products in Car shop")
public class CarsController {

    private final CarService carService;

    @ResponseStatus(value = NOT_FOUND)
    public static class ResourceNotFoundException extends NullPointerException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }


    @ApiOperation(value = "Add a car to the cars shop")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add car"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping(value = "/cars")
    public long addCar(@RequestBody Car car,
                       @RequestParam("price") double price,
                       @RequestParam("ownerContacts") String ownerContacts) {

        long id = carService.addCar(car, price, ownerContacts);

        log.info("addCar was finished [carId: {}; carInfo: {}]", id, car);

        return id;
    }

    @ApiOperation(value = "View a list of available cars sale info in shop", response = CarSaleInfo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned carSaleInfo for all cars"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "CarSaleInfo not found")})
    @GetMapping(value = "/cars")
    public List<CarSaleInfo> getAllCars() {
        return carService.getAllCars();
    }

    @ApiOperation(value = "Delete car by car_id from car shop")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted car by id"),
            @ApiResponse(code = 400, message = "Bad request")})
    @DeleteMapping(value = "/cars/{id}")
    public void deleteCar(@PathVariable("id") long id) {
        carService.deleteCar(id);
    }

    @ApiOperation(value = "Get sale info for car by id", response = SaleInfo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned sale info for car by id"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "SaleInfo for car by id is not found")})
    @GetMapping(value = "/cars/{id}")
    public SaleInfo getCarDetails(@PathVariable("id") long id) {
        try {
            return carService.getSaleInfo(id).orElseThrow(NullPointerException::new);
        } catch (NullPointerException e) {
            throw new ResourceNotFoundException("There is no Sale Info for car with id  " + id);
        }
    }



    @PostMapping(value = "/rejectDeal/{id}")
    public void rejectDeal(@PathVariable("id") long id) {
        carService.rejectDeal(id);
        log.info("Deal with id [id: {}] was rejected", id);

    }


    @PutMapping(value = "/car")
    public long putCarToSale(@RequestBody CarOnSaleRequest carOnSaleRequest) {

       return carService.putCarToSale(carOnSaleRequest);

    }

    @GetMapping(value = "/bestDeal/{advertId}")
    public long chooseBestDeal(@PathVariable("advertId") long advertId){
        return carService.chooseBestDealByAdvertId(advertId);
    }

    @PostMapping(value = "/deal")
    public long createDeal(@RequestBody DealRequest dealRequest,
                           @RequestParam("advertId") long advertId) {
        long dealId = carService.createDeal(dealRequest, advertId);
        log.info("Deal with id [dealId: {}] was created", dealId);
        return dealId;

    }

}