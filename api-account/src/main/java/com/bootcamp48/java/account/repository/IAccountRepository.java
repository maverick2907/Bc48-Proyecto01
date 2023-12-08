package com.bootcamp48.java.account.repository;

import com.bootcamp48.java.account.model.AccountModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountRepository extends RxJava3CrudRepository<AccountModel, String> {
    Maybe<Boolean> existsByIdCustomerAndAccountType(String customerId, String accountType);
    Flowable<AccountModel> findByIdCustomer(String customerId);

}
