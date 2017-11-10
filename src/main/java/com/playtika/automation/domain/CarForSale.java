package com.playtika.automation.domain;

import lombok.Data;

@Data
public class CarForSale {

    private long Id;
    private String model;
    private String brand;
    private String ownerContacts;
    private double price;

}
