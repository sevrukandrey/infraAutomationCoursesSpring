package com.playtika.automation.dao.entity;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.*;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.AUTO;

@Entity(name = "client")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document
public class ClientEntity {

    @com.couchbase.client.java.repository.annotation.Id
    private String id;
    @Field
    private String name;
    @Field
    private String surname;
    @Field
    private String phoneNumber;


    @javax.persistence.Id
    @GeneratedValue(strategy = AUTO)
    public Long getId() {
        return Long.valueOf(id);
    }

    public void setId(Long id) {
        this.id = id.toString();
    }
}
