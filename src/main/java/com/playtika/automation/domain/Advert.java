package com.playtika.automation.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Advert {
    private long id;
    private Car car;
    private Client client;
    private long dealId;
    private double price;
    private AdvertStatus status;
}
