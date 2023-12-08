package com.bootcamp48.java.movement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovementRequest {

    private double totalAmount;
    private String type;
    private Integer idCustomer;

}
