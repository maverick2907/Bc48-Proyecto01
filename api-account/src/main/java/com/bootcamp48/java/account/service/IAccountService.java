package com.bootcamp48.java.account.service;

import com.bootcamp48.java.account.dtos.AccountRequest;
import com.bootcamp48.java.account.model.AccountModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;


public interface IAccountService {
    Flowable<AccountModel> findAll();
    Maybe<AccountModel> create(AccountRequest accountRequest);
    Maybe<AccountModel> update(AccountModel accountModel);
    Maybe<AccountModel> findById(String id);
    Flowable<AccountModel> findByIdCustomer(String id);

}
