package com.bootcamp48.java.movement.message;

import com.bootcamp48.java.movement.model.MovementModel;
import com.bootcamp48.java.movement.service.IMovementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class TransactionConsumerListenerManual implements AcknowledgingMessageListener<Integer, String> {

    private Logger log = LoggerFactory.getLogger(TransactionConsumerListenerManual.class);

    private final IMovementService movementService;
    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void onMessage(ConsumerRecord<Integer, String> data, Acknowledgment acknowledgment) {
        log.info("Consumer Receives in Microservice movement");
        log.info("ConsumerRecord : {}", data.value());

        try {
            log.info("****************************************************************");
            log.info("****************************************************************");

            TransactionRequest requestEvent = objectMapper.readValue(data.value(), TransactionRequest.class);
            log.info("Id Transaction: {} - Type: {} - Amount: {} - Id Product: {}", requestEvent.getId(), requestEvent.getType(),
                    requestEvent.getAmount(), requestEvent.getProductId());

            MovementModel movementModel = new MovementModel();
            movementModel.setIdTransaction(requestEvent.getId());
            movementModel.setAmount(requestEvent.getAmount());
            movementModel.setType(requestEvent.getType());
            movementModel.setProductId(requestEvent.getProductId());
            movementModel.setCreationDate(requestEvent.getCreationDate());
            movementModel.setProcessDate(LocalDateTime.now().toString());

            Completable.fromAction(() -> {
                        this.movementService.create(movementModel)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .blockingGet();
                        acknowledgment.acknowledge();
                    })
                    .subscribe(
                            () -> log.info("Register movement {}", requestEvent.getProductId()),
                            error -> log.error("Error processing JSON", error)
                    );

            log.info("****************************************************************");
            log.info("****************************************************************");

        } catch (JsonProcessingException e) {
            log.error("Error processing JSON", e);
        }
    }
}
