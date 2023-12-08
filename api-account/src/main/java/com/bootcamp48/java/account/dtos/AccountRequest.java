package com.bootcamp48.java.account.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    private double totalAmount;
    private String accountType;
    private int monthlyTransactionLimit;
    private String withdrawalDay;
    private String idCustomer;

}
