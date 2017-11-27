package com.playtika.automation.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class SaleInfo {
    private final String ownerContacts;
    private final double price;
}
