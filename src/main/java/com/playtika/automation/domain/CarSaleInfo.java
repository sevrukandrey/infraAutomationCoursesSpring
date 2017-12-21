package com.playtika.automation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CarSaleInfo {
    @ApiModelProperty(notes = "The database generated car ID")
    private final long id;
    @ApiModelProperty(notes = "Car ")
    private final Car car;
    @ApiModelProperty(notes = "Sale info of current car")
    private final SaleInfo saleInfo;

}
