package com.bootcamp48.java.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionTypeEnum {
    DEPOSIT("DEPOSIT"), CREDIT_PAY("CREDIT_PAY"), WITHDRAWAL("WITHDRAWAL"), CREDIT_CONSUMPTION("CREDIT_CONSUMPTION");
    private final String value;
}
