package com.playtika.automation.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Car {

    private  String brand;
    private  String model;
    private  String plateNumber;
    private  String color;
    private  int year;
}
