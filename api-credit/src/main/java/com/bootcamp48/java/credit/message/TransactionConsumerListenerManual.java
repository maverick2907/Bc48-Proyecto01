package com.bootcamp48.java.credit.message;

import com.bootcamp48.java.credit.model.TransactionTypeEnum;
import com.bootcamp48.java.credit.service.ICreditService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TransactionConsumerListenerManual implements AcknowledgingMessageListener<Integer, String> {

    private Logger log = LoggerFactory.getLogger(TransactionConsumerListenerManual.class);

    private final ICreditService creditService;

    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void onMessage(ConsumerRecord<Integer, String> data, Acknowledgment acknowledgment) {
        log.info("Consumer Receives in Microservice credit");
        log.info("ConsumerRecord : {}", data.value());

        try {
            log.info("****************************************************************");
            log.info("****************************************************************");

            TransactionRequest requestEvent = objectMapper.readValue(data.value(), TransactionRequest.class);
            log.info("Id Transaction: {} - Type: {} - Amount: {} - Id Credit: {}", requestEvent.getId(), requestEvent.getType(),
                    requestEvent.getAmount(), requestEvent.getProductId());

            this.creditService.findById(requestEvent.getProductId())
                    .flatMap(existingAccount -> {
                        if (TransactionTypeEnum.CREDIT_PAY.getValue().equals(requestEvent.getType())) {
                            existingAccount.setTotalCredit(requestEvent.getAmount() + existingAccount.getTotalCredit());
                        } else if (TransactionTypeEnum.CREDIT_CONSUMPTION.getValue().equals(requestEvent.getType())) {
                            if(existingAccount.getTotalCredit() < requestEvent.getAmount()){
                                log.error("Insufficient credit - Available credit: {}", existingAccount.getTotalCredit());
                                throw new IllegalArgumentException("Insufficient credit - Available credit");
                            }else{
                                existingAccount.setTotalCredit(existingAccount.getTotalCredit() - requestEvent.getAmount());
                            }
                        }
                        return this.creditService.update(existingAccount);
                    })
                    .doOnSuccess(updatedAccount -> {
                        log.info("Update credit {}", requestEvent.getProductId());
                        acknowledgment.acknowledge();
                    })
                    .onErrorResumeNext(error -> {
                        log.error("Error processing message");
                        acknowledgment.acknowledge();
                        return Maybe.empty();
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe();

        } catch (JsonProcessingException e) {
            log.error("Error deserializing JSON", e);
            acknowledgment.acknowledge();
        }

        log.info("****************************************************************");
        log.info("****************************************************************");
    }

}