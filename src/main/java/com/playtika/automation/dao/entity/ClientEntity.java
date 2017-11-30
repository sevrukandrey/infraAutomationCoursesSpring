package com.playtika.automation.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Entity(name = "client")
@Getter
@Setter
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private long id;
    private String name;
    private String surname;
    private String phoneNumber;
}
