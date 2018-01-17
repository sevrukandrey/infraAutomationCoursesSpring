package com.playtika.automation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CarOnSaleRequest {
    private Car car;
    private Client client;
    private double price;
}
