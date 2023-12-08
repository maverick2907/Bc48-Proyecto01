package com.bootcamp48.java.client.service;

import com.bootcamp48.java.client.dtos.ClientRequest;
import com.bootcamp48.java.client.model.ClientModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public interface IClientService {

    Flowable<ClientModel> getAll();
    Single<ClientModel> add(ClientRequest clientRequest);
    Maybe<ClientModel> getById(String idCustomer);
}
