package com.bootcamp48.java.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionTypeEnum {
    DEPOSIT("DEPOSIT"), WITHDRAWAL("WITHDRAWAL");
    private final String value;
}
