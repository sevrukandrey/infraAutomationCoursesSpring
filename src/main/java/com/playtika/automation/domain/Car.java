package com.playtika.automation.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class Car {

    private long id;
    private String brand;
    private String ownerContacts;
    private double price;
}
