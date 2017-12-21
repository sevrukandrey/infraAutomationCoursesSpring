package com.playtika.automation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Car {

    @ApiModelProperty(notes = "Car brand")
    private  String brand;
    @ApiModelProperty(notes = "Car model")
    private  String model;
    @ApiModelProperty(required = true, notes = "Car plate number")
    private  String plateNumber;
    @ApiModelProperty(notes = "Car color")
    private  String color;
    @ApiModelProperty(notes = "Car year of manufacture")
    private  int year;
}
