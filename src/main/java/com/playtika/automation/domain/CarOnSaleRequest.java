package com.playtika.automation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CarOnSaleRequest {


    private  String brand;
    private  String model;
    private  String plateNumber;
    private  String color;
    private  int year;
    private  String name;
    private  String sureName;
    private  String phoneNumber;
    private double price;


}
