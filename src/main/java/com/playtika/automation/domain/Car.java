package com.playtika.automation.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Car {

    private long id;
    private String brand;
    private String ownerContacts;
    private double price;
}
