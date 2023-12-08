package com.bootcamp48.java.credit.service;

import com.bootcamp48.java.credit.dtos.CreditRequest;
import com.bootcamp48.java.credit.model.CreditModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;


public interface ICreditService {
    Flowable<CreditModel> findAll();
    Maybe<CreditModel> create(CreditRequest creditRequest);
    Maybe<CreditModel> update(CreditModel creditModel);
    Maybe<CreditModel> findById(String id);
    Flowable<CreditModel> findByIdCustomer(String id);

}
