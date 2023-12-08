package com.bootcamp48.java.client.model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customer")
public class ClientModel {
    @BsonId
    private String id;
    private String fullname;
    private String type;
    private String email;
}
