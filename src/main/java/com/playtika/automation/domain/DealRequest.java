package com.playtika.automation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DealRequest {
    private  String name;
    private  String sureName;
    private  String phoneNumber;
    private double price;
}
