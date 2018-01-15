package com.playtika.automation.dao.entity;

import com.playtika.automation.domain.AdvertStatus;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;

@Entity(name = "advert")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "deal_id")
    private Long dealId;

    private double price;

    @Enumerated(STRING)
    private AdvertStatus status;

    public AdvertEntity(CarEntity car, ClientEntity client, Long dealId, double price, AdvertStatus status) {
        this.car = car;
        this.client = client;
        this.dealId = dealId;
        this.price = price;
        this.status = status;
    }

    public AdvertEntity(Long id, double price, Long dealId) {
        this.id = id;
        this.price = price;
        this.dealId = dealId;
    }

}
