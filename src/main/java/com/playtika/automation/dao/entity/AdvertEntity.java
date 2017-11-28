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

    @ManyToOne()
    @JoinColumn(name = "id")
    private CarEntity carId;

    @ManyToOne()
    @JoinColumn(name = "id")
    private ClientEntity sellerId;
    private Double price;

    @ManyToOne()
    @JoinColumn(name = "id")
    private Long dealId;

    private String status = "Open";

}
