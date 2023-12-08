package com.bootcamp48.java.transaction.model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transaction")
public class TransactionModel {
    @BsonId
    private String id;
    private double amount;
    private String type;
    private String creationDate;
    private String productId;

}
