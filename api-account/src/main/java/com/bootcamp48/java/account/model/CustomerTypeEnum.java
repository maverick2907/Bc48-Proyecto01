package com.bootcamp48.java.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerTypeEnum {
    PERSONAL("PERSONAL"), BUSINESS("BUSINESS");
    private final String value;
}
