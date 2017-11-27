package com.playtika.automation.domain;

import lombok.*;

@Data
public class Car {

    private final String brand;
    private final String model;
    private final String plateNumber;
    private final String color;
    private final int year;
}
