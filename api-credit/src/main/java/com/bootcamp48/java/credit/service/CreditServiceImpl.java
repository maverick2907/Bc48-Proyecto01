package com.bootcamp48.java.credit.service;

import com.bootcamp48.java.credit.dtos.CreditRequest;
import com.bootcamp48.java.credit.model.CreditTypeEnum;
import com.bootcamp48.java.credit.repository.ICreditRepository;
import com.bootcamp48.java.credit.model.CreditModel;
import com.bootcamp48.java.credit.web.model.ClientModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CreditServiceImpl implements ICreditService {

    Logger logger = LoggerFactory.getLogger(CreditServiceImpl.class);

    private final ICreditRepository creditRepository;

    @Override
    public Flowable<CreditModel> findAll() {
        return this.creditRepository.findAll();
    }

    @Override
    public Maybe<CreditModel> create(CreditRequest creditRequest) {
        logger.info("Creating credit for Customer ID: {} - Credit Type: {} - Total Credit: {}",
                creditRequest.getIdCustomer(), creditRequest.getCreditType(), creditRequest.getTotalCredit());

        return getClientById(creditRequest.getIdCustomer())
                .flatMap(clientModel -> createCreditForClient(creditRequest, clientModel))
                .onErrorResumeNext(error -> {
                    logger.error("Error creating credit for Customer ID {}: {}", creditRequest.getIdCustomer(), error.getMessage());
                    return Maybe.error(error);
                });
    }

    @Override
    public Maybe<CreditModel> update(CreditModel creditModel) {
        logger.info("Updating credit with ID: {}", creditModel.getId());

        return this.creditRepository.save(creditModel)
                .doOnSuccess(updatedAccount -> logger.info("Credit updated successfully"))
                .doOnError(error -> logger.error("Error updating credit: {}", error.getMessage()))
                .toMaybe();
    }

    @Override
    public Maybe<CreditModel> findById(String id) {
        return this.creditRepository.findById(id);
    }

    @Override
    public Flowable<CreditModel> findByIdCustomer(String id) {
        return this.creditRepository.findByIdCustomer(id);
    }

    private Maybe<CreditModel> createCreditForClient(CreditRequest creditRequest, ClientModel clientModel) {
        String clientType = clientModel.getType();

        return CreditTypeEnum.PERSONAL.getValue().equals(clientType)
                ? createPersonalCredit(creditRequest)
                : CreditTypeEnum.BUSINESS.getValue().equals(clientType)
                ? createBusinessCredit(creditRequest)
                : Maybe.error(new IllegalArgumentException("Unsupported client type: " + clientType));
    }

    private Maybe<CreditModel> createPersonalCredit(CreditRequest creditRequest) {
        return this.creditRepository.existsByIdCustomerAndCreditType(creditRequest.getIdCustomer(), creditRequest.getCreditType())
                .flatMap(exist -> {
                    if (exist) {
                        return Maybe.error(new IllegalArgumentException("Client must only have a personal credit or credit card."));
                    } else if (CreditTypeEnum.BUSINESS.getValue().equals(creditRequest.getCreditType()) ||
                            CreditTypeEnum.CREDITCARD_BUSINESS.getValue().equals(creditRequest.getCreditType())) {
                        return Maybe.error(new IllegalArgumentException("Client cannot have a credit of this type"));
                    } else {
                        return createAndSaveCredit(creditRequest);
                    }
                });
    }


    private Maybe<CreditModel> createBusinessCredit(CreditRequest creditRequest) {
        return CreditTypeEnum.PERSONAL.getValue().equals(creditRequest.getCreditType()) || CreditTypeEnum.CREDITCARD_PERSONAL.getValue().equals(creditRequest.getCreditType())
                ? Maybe.error(new IllegalArgumentException("Business client cannot have an credit of this type"))
                : createAndSaveCredit(creditRequest);
    }

    private Maybe<CreditModel> createAndSaveCredit(CreditRequest creditRequest) {
        CreditModel creditModel = new CreditModel();
        creditModel.setCreditType(creditRequest.getCreditType());
        if(creditRequest.getTotalCredit() <= 0){
            creditModel.setTotalCredit(0);
        }else{
            creditModel.setTotalCredit(creditRequest.getTotalCredit());
        }
        creditModel.setCreationDate(LocalDateTime.now().toString());
        creditModel.setMaxCreditsPerPerson(creditRequest.getMaxCreditsPerPerson());
        creditModel.setMaxCreditsPerCompany(creditRequest.getMaxCreditsPerCompany());
        creditModel.setIdCustomer(creditRequest.getIdCustomer());

        logger.info("creditModel {}", creditModel);

        return creditRepository.save(creditModel).toMaybe();
    }

    private Single<WebClient> getApiClient() {
        logger.debug("getApiClient executed");

        return Single.fromCallable(() ->
                WebClient.builder()
                        .baseUrl("http://localhost:9104/v1/client")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .build()
        ).subscribeOn(Schedulers.io());
    }

    private Maybe<ClientModel> getClientById(String idCustomer) {
        logger.debug("getClientById executed for ID: {}", idCustomer);

        return getApiClient()
                .flatMap(webClient ->
                        Single.fromCallable(() ->
                                webClient.get().uri("/" + idCustomer)
                                        .retrieve()
                                        .bodyToMono(ClientModel.class)
                                        .doOnSuccess(clientModel ->
                                                logger.info("ClientModel retrieved successfully for ID: {}", idCustomer))
                                        .onErrorResume(throwable -> {
                                            logger.error("Error retrieving ClientModel for ID {}: {}", idCustomer, throwable.getMessage());
                                            return Mono.empty();
                                        }).block()
                        )
                )
                .toMaybe()
                .subscribeOn(Schedulers.io());
    }

}
