package com.playtika.automation.dao.entity;

import com.playtika.automation.domain.DealStatus;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;

@Entity(name = "deal")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DealEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private ClientEntity buyer;

    private double price;

    @ManyToOne
    @JoinColumn(name = "advert_id")
    private AdvertEntity advert;
    @Enumerated(STRING)
    private DealStatus status;

    public DealEntity(ClientEntity buyer, double price, AdvertEntity advert, DealStatus status) {
        this.buyer = buyer;
        this.price = price;
        this.advert = advert;
        this.status = status;
    }

}
