package com.playtika.automation.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity(name = "car")
@Getter
@Setter
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String plateNumber;
    private  int year;
    private String color;
    private String model;

    @OneToMany(mappedBy = "id")
    List<ClientEntity> clientEntities;

    @OneToMany(mappedBy = "id")
    List<AdvertEntity> advertEntities;
}
