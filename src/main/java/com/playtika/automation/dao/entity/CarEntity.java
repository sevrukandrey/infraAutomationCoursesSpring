package com.playtika.automation.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;


@Entity(name = "car")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Document
public class CarEntity {

    // @com.couchbase.client.java.repository.annotation.Id
   @Id
   @GeneratedValue(strategy = AUTO)
    private Long id;

    //@Field
    private String plateNumber;
  //  @Field
    private int year;
   // @Field
    private String color;
   // @Field
    private String model;
  //  @Field
    private String brand;

    public CarEntity(String plateNumber, int year, String color, String model, String brand) {
        this.plateNumber = plateNumber;
        this.year = year;
        this.color = color;
        this.model = model;
        this.brand = brand;
    }
//
//   @javax.persistence.Id
//    @GeneratedValue(strategy = AUTO)
//    public Long getId() {
//        return Long.valueOf(id);
//    }
//
//    public void setId(Long id) {
//        this.id = id.toString();
//    }
}
