package com.bootcamp48.java.account.message;

import com.bootcamp48.java.account.model.AccountModel;
import com.bootcamp48.java.account.model.TransactionTypeEnum;
import com.bootcamp48.java.account.service.IAccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class TransactionConsumerListenerManual implements AcknowledgingMessageListener<Integer, String> {

    private Logger log = LoggerFactory.getLogger(TransactionConsumerListenerManual.class);

    private final IAccountService accountService;

    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void onMessage(ConsumerRecord<Integer, String> data, Acknowledgment acknowledgment) {
        log.info("Consumer Receives in Microservice account");
        log.info("ConsumerRecord : {}", data.value());

        try {
            log.info("****************************************************************");
            log.info("****************************************************************");

            TransactionRequest requestEvent = objectMapper.readValue(data.value(), TransactionRequest.class);
            log.info("Id Transaction: {} - Type: {} - Amount: {} - Id Account: {}", requestEvent.getId(), requestEvent.getType(),
                    requestEvent.getAmount(), requestEvent.getProductId());

            this.accountService.findById(requestEvent.getProductId())
                    .flatMap(existingAccount -> {
                        if (TransactionTypeEnum.DEPOSIT.getValue().equals(requestEvent.getType())) {
                            existingAccount.setTotalAmount(requestEvent.getAmount() + existingAccount.getTotalAmount());
                        } else if (TransactionTypeEnum.WITHDRAWAL.getValue().equals(requestEvent.getType())) {
                            if(existingAccount.getTotalAmount() < requestEvent.getAmount()){
                                throw new IllegalArgumentException("Insufficient money - Available money: "
                                        + existingAccount.getTotalAmount());
                            }else{
                                existingAccount.setTotalAmount(existingAccount.getTotalAmount() - requestEvent.getAmount());
                            }
                        }
                        return this.accountService.update(existingAccount);
                    })
                    .doOnSuccess(updatedAccount -> {
                        log.info("Update product {}", requestEvent.getProductId());
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