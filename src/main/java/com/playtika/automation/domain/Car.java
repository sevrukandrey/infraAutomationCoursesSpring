package com.playtika.automation.domain;

import lombok.Builder;
import lombok.Data;


public class Car {
    public long id;
    public String brand;
    public int year;
    public String color;
    public int price;
    public boolean sold;

    public Car(){

    }

    public Car(long id, String brand, int year, String color, int price, boolean sold) {
        this.id = id;
        this.brand = brand;
        this.year = year;
        this.color = color;
        this.price = price;
        this.sold = sold;
    }
}
