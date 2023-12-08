package com.bootcamp48.java.credit.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequest {
    private double totalCredit;
    private String creditType;
    private int maxCreditsPerPerson;
    private int maxCreditsPerCompany;
    private String idCustomer;
}
