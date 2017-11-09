package com.playtika.automation.controller.dto;

import lombok.Data;

@Data
public class CarForSale {


    private long id;
    private String brand;
    private String model;
    private String ownerContacts;
    private double price;
}
