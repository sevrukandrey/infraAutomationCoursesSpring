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
public class DealRequest {
    @ApiModelProperty(required = true, notes = "Client")
    private Client client;
    @ApiModelProperty(required = true, notes = "Client")
    private double price;
}
