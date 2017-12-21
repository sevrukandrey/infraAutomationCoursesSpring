package com.playtika.automation.dao.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;


@Entity(name = "car")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CarEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String plateNumber;
    private int year;

    private String color;
    private String model;
    private String brand;

    public CarEntity(String plateNumber, int year, String color, String model, String brand) {
        this.plateNumber = plateNumber;
        this.year = year;
        this.color = color;
        this.model = model;
        this.brand = brand;
    }
}

