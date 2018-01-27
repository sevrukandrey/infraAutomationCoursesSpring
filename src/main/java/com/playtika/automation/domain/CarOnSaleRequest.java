package com.playtika.automation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CarOnSaleRequest {
    @ApiModelProperty(required = true, notes = "Car for sale")
    private Car car;
    @ApiModelProperty(required = true, notes = "Seller info")
    private Client client;
    @ApiModelProperty(required = true, notes = "Proposed price")
    private double price;
}
