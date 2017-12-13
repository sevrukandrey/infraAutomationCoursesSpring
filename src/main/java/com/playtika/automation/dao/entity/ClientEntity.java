package com.playtika.automation.dao.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Entity(name = "client")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private long id;
    private String name;

    private String surname;
    private String phoneNumber;

    public ClientEntity(String name, String surname, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }
}
