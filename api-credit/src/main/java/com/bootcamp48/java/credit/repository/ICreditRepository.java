package com.bootcamp48.java.credit.repository;

import com.bootcamp48.java.credit.model.CreditModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICreditRepository extends RxJava3CrudRepository<CreditModel, String> {
    Maybe<Boolean> existsByIdCustomerAndCreditType(String customerId, String accountType);
    Flowable<CreditModel> findByIdCustomer(String customerId);

}
