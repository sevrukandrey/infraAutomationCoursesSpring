package com.playtika.automation.domain;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class CarSaleInfo {

    private final long id;
    private final Car car;
    private final SaleInfo saleInfo;

}
