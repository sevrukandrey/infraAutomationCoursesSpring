package com.playtika.automation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@AllArgsConstructor
public class SaleInfo {
    @ApiModelProperty(notes = "Owner phone number")
    private String ownerContacts;
    @ApiModelProperty(notes = "Price of car")
    private double price;
}
