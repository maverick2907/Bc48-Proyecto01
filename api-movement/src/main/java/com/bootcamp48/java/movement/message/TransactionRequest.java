package com.bootcamp48.java.movement.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    private String id;
    private double amount;
    private String type;
    private String creationDate;
    private String productId;

}
