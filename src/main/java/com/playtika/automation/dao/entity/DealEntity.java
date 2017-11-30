package com.playtika.automation.dao.entity;

import com.playtika.automation.domain.DealStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.EnumType.*;
import static javax.persistence.GenerationType.AUTO;

@Entity(name = "deal")
@Getter
@Setter
public class DealEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private long buyerId;
    private double price;
    private long advertId;
    @Enumerated(STRING)
    private DealStatus status;
}
