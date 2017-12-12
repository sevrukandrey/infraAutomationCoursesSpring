package com.playtika.automation.dao.entity;

import com.couchbase.client.java.repository.annotation.Field;
import org.springframework.data.couchbase.core.mapping.Document;

@Document
public class ClientCouch {
    @com.couchbase.client.java.repository.annotation.Id
    private long id;
    @Field
    private String name;
    @Field
    private String surname;
    @Field
    private String phoneNumber;
}
