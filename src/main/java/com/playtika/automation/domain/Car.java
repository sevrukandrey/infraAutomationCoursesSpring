package com.playtika.automation.domain;

import lombok.Builder;
import lombok.Data;


public class Car {
    public long id;
    public String brand;

    public Car(){

    }

    public Car(long id, String brand) {
        this.id = id;
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                '}';
    }
}
