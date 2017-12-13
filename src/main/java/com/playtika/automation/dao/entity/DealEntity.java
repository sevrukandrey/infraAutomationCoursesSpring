package com.playtika.automation.dao.entity;

import com.playtika.automation.domain.DealStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;

@Entity(name = "deal")
@Getter
@Setter
@Document
public class DealEntity {


    @com.couchbase.client.java.repository.annotation.Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private ClientEntity buyer;
    private double price;
    @ManyToOne
    @JoinColumn(name = "advert_id")
    private AdvertEntity advert;
    @Enumerated(STRING)
    private DealStatus status;


    @javax.persistence.Id
    @GeneratedValue(strategy = AUTO)
    public Long getId() {
        return Long.valueOf(id);
    }

    public void setId(Long id) {
        this.id = id.toString();
    }
}
