package com.bootcamp48.java.credit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CreditTypeEnum {
    PERSONAL("PERSONAL"), BUSINESS("BUSINESS"), CREDITCARD_PERSONAL("CREDITCARD_PERSONAL"), CREDITCARD_BUSINESS("CREDITCARD_BUSINESS");
    private final String value;
}
