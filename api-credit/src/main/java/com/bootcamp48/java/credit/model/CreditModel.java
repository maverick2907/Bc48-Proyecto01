package com.bootcamp48.java.credit.model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "account")
public class CreditModel {
    @BsonId
    private String id; //Id del producto
    private double totalCredit; //Monto total del credito
    private String creationDate; //Fecha de creación de la cuenta de credito
    private String creditType;  //Puede ser "Personal", "Empresarial", "Tarjeta de Crédito Personal", "Tarjeta de Crédito Empresarial", etc.
    private int maxCreditsPerPerson;  //Número máximo de créditos permitidos por persona (puede ser 0 si no es aplicable)
    private int maxCreditsPerCompany;  //Número máximo de créditos permitidos por empresa (puede ser 0 si no es aplicable)
    private String idCustomer; //Id del cliente
}
