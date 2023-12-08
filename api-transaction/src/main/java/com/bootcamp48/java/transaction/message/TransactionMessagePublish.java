package com.bootcamp48.java.transaction.message;

import com.bootcamp48.java.transaction.model.TransactionModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionMessagePublish {

    @Value("${spring.kafka.template.default-topic}")
    String topicName;
    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;
    @Autowired
    ObjectMapper objectMapper;

    public void sendDepositEvent(TransactionModel transactionModel) throws JsonProcessingException {

        String value = objectMapper.writeValueAsString(transactionModel);
        kafkaTemplate.send(topicName,value);
    }

}
