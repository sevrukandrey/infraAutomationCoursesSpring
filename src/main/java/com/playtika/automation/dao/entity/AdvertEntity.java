package com.playtika.automation.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "advert")
@Getter
@Setter
public class AdvertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //@OneToOne
    private Long carId;
   // @OneToOne
    private Long sellerId;
    private Double price;
   // @OneToOne
    private Long dealId;

    private String status = "Open";

}
