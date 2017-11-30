package com.playtika.automation.dao.entity;

import com.playtika.automation.domain.AdvertStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;

@Entity(name = "advert")
@Getter
@Setter
public class AdvertEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @ManyToOne(cascade = REMOVE)
    @JoinColumn(name = "car_id")
    private CarEntity car;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private ClientEntity client;

    @ManyToOne()
    @JoinColumn(name = "deal_id")
    private DealEntity deal;

    private double price;

    @Enumerated(STRING)
    private AdvertStatus status;
}
