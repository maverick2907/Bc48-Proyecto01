package com.bootcamp48.java.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountTypeEnum {
    SAVINGS("SAVINGS"), CHECKING_ACCOUNT("CHECKING_ACCOUNT"), FIXED_TERM("FIXED_TERM");
    private final String value;
}
