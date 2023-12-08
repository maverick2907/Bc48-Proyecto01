package com.bootcamp48.java.credit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerTypeEnum {
    PERSONAL("PERSONAL"), BUSINESS("BUSINESS");
    private final String value;
}
