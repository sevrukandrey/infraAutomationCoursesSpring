package com.playtika.automation.domain;

import lombok.Data;

@Data
public class Car {
    private String id;
    private String brand;
    private int year;
    private String color;
    private int price;
    private boolean sold;

}
