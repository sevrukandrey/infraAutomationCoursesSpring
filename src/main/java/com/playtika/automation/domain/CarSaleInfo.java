package com.playtika.automation.domain;

import lombok.Data;

@Data
public class CarSaleInfo {

    private final long id;
    private final Car car;
    private final SaleInfo saleInfo;

}
