package com.playtika.automation.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deal {
    private Long id;
    private Client client;
    private double price;
    private Advert advert;
    private DealStatus status;
}
