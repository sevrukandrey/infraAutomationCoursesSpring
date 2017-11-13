package com.playtika.automation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class CarForSale {

    private long id;
    private String model;
    private String brand;
    private String ownerContacts;
    private double price;

}
