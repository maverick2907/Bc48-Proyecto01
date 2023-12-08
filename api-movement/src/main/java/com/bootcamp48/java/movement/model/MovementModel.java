package com.bootcamp48.java.movement.model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "movement")
public class MovementModel {

    @BsonId
    private String id ;
    private String idTransaction;
    private double amount ;
    private String type;
    private String productId;
    private String creationDate;
    private String processDate;

}
