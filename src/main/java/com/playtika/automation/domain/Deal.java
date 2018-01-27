package com.playtika.automation.domain;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deal {
    @ApiModelProperty( notes = "Deal id")
    private Long id;
    @ApiModelProperty( notes = "Client")
    private Client client;
    @ApiModelProperty( notes = "price")
    private double price;
    @ApiModelProperty( notes = "advert")
    private Advert advert;
    @ApiModelProperty( notes = "DealStatus")
    private DealStatus status;
}
