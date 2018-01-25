package com.playtika.automation.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deal {

    private long id;
    private Client client;
    private double price;
    private long advertId;
    private DealStatus status;


}
