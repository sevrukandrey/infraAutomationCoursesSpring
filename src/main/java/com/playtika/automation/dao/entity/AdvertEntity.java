package com.playtika.automation.dao.entity;

import com.playtika.automation.domain.AdvertStatus;
import lombok.*;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.persistence.*;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;

@Entity(name = "advert")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Document
public class AdvertEntity {

   // @com.couchbase.client.java.repository.annotation.Id
   @Id
   @GeneratedValue(strategy = AUTO)
    private Long id;

 @ManyToOne(cascade = REMOVE)
    @JoinColumn(name = "car_id")
    //@Field
    private CarEntity car;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private ClientEntity client;

    @ManyToOne()
    @JoinColumn(name = "deal_id")
    private DealEntity deal;

    //@Field
    private double price;

    @Enumerated(STRING)
    //@Field
    private AdvertStatus status;

 public AdvertEntity(CarEntity car, ClientEntity client, DealEntity deal, double price, AdvertStatus status) {
  this.car = car;
  this.client = client;
  this.deal = deal;
  this.price = price;
  this.status = status;
 }

//    @Id
//    @GeneratedValue(strategy = AUTO)
//    public Long getId() {
//        return Long.valueOf(id);
//    }
//
//    public void setId(Long id) {
//        this.id = id.toString();
//    }

}
