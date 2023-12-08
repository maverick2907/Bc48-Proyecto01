package com.bootcamp48.java.client.service;

import com.bootcamp48.java.client.dtos.ClientRequest;
import com.bootcamp48.java.client.model.ClientModel;
import com.bootcamp48.java.client.repository.IClientRepository;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements IClientService {
    Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
    private final IClientRepository clientRepository;

    @Override
    public Flowable<ClientModel> getAll() {
        return this.clientRepository.findAll();
    }

    @Override
    public Single<ClientModel> add(ClientRequest clientRequest) {
        // Validation
        if (clientRequest == null ||
                StringUtils.isBlank(clientRequest.getFullname()) ||
                StringUtils.isBlank(clientRequest.getEmail()) ||
                clientRequest.getType() == null) {
            return Single.error(new IllegalArgumentException("Incorrect input data."));
        }

        ClientModel clientModel = new ClientModel();
        clientModel.setFullname(clientRequest.getFullname());
        clientModel.setEmail(clientRequest.getEmail());
        clientModel.setType(clientRequest.getType());

        return Single.just(clientModel)
                .map(model -> {
                    logger.info("clientModel {}", model);
                    return model;
                })
                .flatMap(this.clientRepository::save)
                .onErrorResumeNext(error -> {
                    //Handle save to repository error
                    logger.error("Error saving to repository: {}", error.getMessage());
                    return Single.error(error);
                });
    }

    @Override
    public Maybe<ClientModel> getById(String idCustomer) {
        return this.clientRepository.findById(idCustomer);
    }

}
