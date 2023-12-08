package com.bootcamp48.java.account.model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "account")
public class AccountModel {
    @BsonId
    private String id; //Id del producto
    private double totalAmount; //Monto total de la cuenta
    private String creationDate; //Fecha de creación de la cuenta
    private String accountType;  //Puede ser "Ahorro", "Cuenta corriente", "Plazo fijo", etc.
    private boolean commissionFree;  //Indica si la cuenta tiene comisión de mantenimiento o no
    private int monthlyTransactionLimit;  //Límite máximo de movimientos mensuales (puede ser 0 si no hay límite)
    private String withdrawalDay;  //Día específico del mes para retiros en cuentas a plazo fijo (puede ser null si no es aplicable)
    private String idCustomer; //Id del cliente

}
