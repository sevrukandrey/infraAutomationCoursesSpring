package com.playtika.automation.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "car")
@Getter
@Setter
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String plateNumber;
    private int year;
    private String color;
    private String model;
    private String brand;
}
