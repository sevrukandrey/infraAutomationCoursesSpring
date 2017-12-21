package com.playtika.automation.dao.entity;

import io.swagger.annotations.ApiModelProperty;
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
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    @ApiModelProperty(notes = "The database generated car ID")
    private Long id;
    @ApiModelProperty(notes = "Client name")
    private String name;
    @ApiModelProperty(notes = "Client surname")
    private String surname;
    @ApiModelProperty(notes = "Client phone number")
    private String phoneNumber;

    public ClientEntity(String name, String surname, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }


}
