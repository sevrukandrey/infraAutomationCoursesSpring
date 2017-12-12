package com.playtika.automation.dao.entity;

import com.couchbase.client.java.repository.annotation.Field;
import com.playtika.automation.domain.DealStatus;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;
@Document
public class DealCouch {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Field
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private ClientEntity buyer;
    @Field
    private double price;
    @Field
    @ManyToOne
    @JoinColumn(name = "advert_id")
    private AdvertEntity advert;
    @Enumerated(STRING)
    @Field
    private DealStatus status;
}
