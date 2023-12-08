package com.bootcamp48.java.credit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionTypeEnum {
    CREDIT_PAY("CREDIT_PAY"), CREDIT_CONSUMPTION("CREDIT_CONSUMPTION");
    private final String value;
}
