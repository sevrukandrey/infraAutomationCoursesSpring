package com.playtika.automation.dao.entity;

import com.couchbase.client.java.repository.annotation.Field;
import org.springframework.data.couchbase.core.mapping.Document;

@Document
public class CarCouch {


    @com.couchbase.client.java.repository.annotation.Id
    private Long id;
    @Field
    private String plateNumber;
    @Field
    private int year;
    @Field
    private String color;
    @Field
    private String model;
    @Field
    private String brand;

    public CarCouch(String plateNumber, int year, String color, String model, String brand) {
        this.plateNumber = plateNumber;
        this.year = year;
        this.color = color;
        this.model = model;
        this.brand = brand;
    }
}

