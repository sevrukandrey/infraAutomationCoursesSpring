package com.playtika.automation.dao.entity;

import lombok.*;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Entity(name = "client")
@Getter
@Setter
@NoArgsConstructor
@ToString
@Document
public class ClientEntity {

   // @com.couchbase.client.java.repository.annotation.Id
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
  //  @Field

    private String name;
  //  @Field
    private String surname;
  //  @Field
    private String phoneNumber;

    public ClientEntity(String name, String surname, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

//    @javax.persistence.Id
//    @GeneratedValue(strategy = AUTO)
//    public Long getId() {
//        return Long.valueOf(id);
//    }
//
//    public void setId(Long id) {
//        this.id = id.toString();
//    }
}
