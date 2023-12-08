package com.bootcamp48.java.transaction.repository;

import com.bootcamp48.java.transaction.model.TransactionModel;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionRepository extends RxJava3CrudRepository<TransactionModel, String> {
}
