package com.bootcamp48.java.transaction.service;

import com.bootcamp48.java.transaction.dtos.TransactionRequest;
import com.bootcamp48.java.transaction.message.TransactionMessagePublish;
import com.bootcamp48.java.transaction.model.TransactionModel;
import com.bootcamp48.java.transaction.repository.ITransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements ITransactionService{
    Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private final ITransactionRepository transactionRepository;
    private final TransactionMessagePublish messageEvent;

    @Override
    @Transactional
    public Maybe<TransactionModel> add(TransactionRequest transactionRequest) {
        logger.info("Post: ProductId {} - Amount {}", transactionRequest.getProductId(), transactionRequest.getAmount());

        TransactionModel transactionModel = createTransactionModel(transactionRequest);

        return Single.just(transactionModel)
                .flatMap(this.transactionRepository::save)
                .doOnSuccess(this::handleSuccess)
                .toMaybe();
    }

    private TransactionModel createTransactionModel(TransactionRequest transactionRequest) {
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setAmount(transactionRequest.getAmount());
        transactionModel.setProductId(transactionRequest.getProductId());
        transactionModel.setType(transactionRequest.getType());
        transactionModel.setCreationDate(LocalDateTime.now().toString());
        logger.info("transactionModel {}", transactionModel);
        return transactionModel;
    }

    private void handleSuccess(TransactionModel savedTransaction) {
        try {
            messageEvent.sendDepositEvent(savedTransaction);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
