package com.bootcamp48.java.transaction.service;

import com.bootcamp48.java.transaction.dtos.TransactionRequest;
import com.bootcamp48.java.transaction.model.TransactionModel;
import io.reactivex.rxjava3.core.Maybe;

public interface ITransactionService {
    Maybe<TransactionModel> add(TransactionRequest transactionRequest);
}
