package com.playtika.automation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @ApiModelProperty(notes = "Client name")
    private  String name;
    @ApiModelProperty(notes = "Client sureName")
    private  String sureName;
    @ApiModelProperty(required = true, notes = "Client phoneNumber")
    private  String phoneNumber;
}
