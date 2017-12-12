package com.playtika.automation.dao.entity;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import com.playtika.automation.domain.AdvertStatus;
import lombok.*;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.persistence.*;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.EnumType.STRING;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdvertCouch {

    @Id
    private Long id;

    @Field
    @ManyToOne(cascade = REMOVE)
    @JoinColumn(name = "car_id")
    private CarCouch car;

    @Field
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private ClientCouch client;

    @Field
    @ManyToOne()
    @JoinColumn(name = "deal_id")
    private DealCouch deal;

    @Field
    private double price;

    @Field
    @Enumerated(STRING)
    private AdvertStatus status;
}
