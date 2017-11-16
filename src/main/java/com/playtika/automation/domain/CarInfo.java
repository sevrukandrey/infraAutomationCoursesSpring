package com.playtika.automation.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarInfo {
    private String ownerContacts;
    private double price;
}
