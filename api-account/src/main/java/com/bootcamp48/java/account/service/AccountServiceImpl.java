package com.bootcamp48.java.account.service;

import com.bootcamp48.java.account.dtos.AccountRequest;
import com.bootcamp48.java.account.model.AccountTypeEnum;
import com.bootcamp48.java.account.model.CustomerTypeEnum;
import com.bootcamp48.java.account.repository.IAccountRepository;
import com.bootcamp48.java.account.model.AccountModel;
import com.bootcamp48.java.account.web.model.ClientModel;
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
public class AccountServiceImpl implements IAccountService {

    Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final IAccountRepository accountRepository;

    @Override
    public Flowable<AccountModel> findAll() {
        return this.accountRepository.findAll();
    }

    @Override
    public Maybe<AccountModel> create(AccountRequest accountRequest) {
        logger.info("Creating account for Customer ID: {} - Account Type: {} - Total Amount: {}",
                accountRequest.getIdCustomer(), accountRequest.getAccountType(), accountRequest.getTotalAmount());

        return getClientById(accountRequest.getIdCustomer())
                .flatMap(clientModel -> createAccountForClient(accountRequest, clientModel))
                .onErrorResumeNext(error -> {
                    logger.error("Error creating account for Customer ID {}: {}", accountRequest.getIdCustomer(), error.getMessage());
                    return Maybe.error(error);
                });
    }

    @Override
    public Maybe<AccountModel> update(AccountModel accountModel) {
        logger.info("Updating account with ID: {}", accountModel.getId());

        return this.accountRepository.save(accountModel)
                .doOnSuccess(updatedAccount -> logger.info("Account updated successfully"))
                .doOnError(error -> logger.error("Error updating account: {}", error.getMessage()))
                .toMaybe();
    }

    @Override
    public Maybe<AccountModel> findById(String id) {
        return this.accountRepository.findById(id);
    }

    @Override
    public Flowable<AccountModel> findByIdCustomer(String id) {
        return this.accountRepository.findByIdCustomer(id);
    }

    private Maybe<AccountModel> createAccountForClient(AccountRequest accountRequest, ClientModel clientModel) {
        String clientType = clientModel.getType();

        return CustomerTypeEnum.PERSONAL.getValue().equals(clientType)
                ? createPersonalAccount(accountRequest)
                : CustomerTypeEnum.BUSINESS.getValue().equals(clientType)
                ? createBusinessAccount(accountRequest)
                : Maybe.error(new IllegalArgumentException("Unsupported client type: " + clientType));
    }

    private Maybe<AccountModel> createPersonalAccount(AccountRequest accountRequest) {
        return accountRepository.existsByIdCustomerAndAccountType(accountRequest.getIdCustomer(), accountRequest.getAccountType())
                .flatMap(exists ->
                        exists
                                ? Maybe.error(new IllegalArgumentException("The client already has an account of this type"))
                                : createAndSaveAccount(accountRequest)
                );
    }

    private Maybe<AccountModel> createBusinessAccount(AccountRequest accountRequest) {
        return AccountTypeEnum.SAVINGS.getValue().equals(accountRequest.getAccountType()) || AccountTypeEnum.FIXED_TERM.getValue().equals(accountRequest.getAccountType())
                ? Maybe.error(new IllegalArgumentException("Business clients cannot have savings or fixed-term accounts."))
                : createAndSaveAccount(accountRequest);
    }

    private Maybe<AccountModel> createAndSaveAccount(AccountRequest accountRequest) {
        AccountModel accountModel = new AccountModel();
        accountModel.setAccountType(accountRequest.getAccountType());
        accountModel.setTotalAmount(accountRequest.getTotalAmount());
        accountModel.setCommissionFree(accountRequest.getAccountType().equals(AccountTypeEnum.SAVINGS.getValue()) || accountRequest.getAccountType().equals(AccountTypeEnum.FIXED_TERM.getValue()));
        accountModel.setMonthlyTransactionLimit(accountRequest.getMonthlyTransactionLimit());
        accountModel.setWithdrawalDay(accountRequest.getWithdrawalDay());
        accountModel.setCreationDate(LocalDateTime.now().toString());
        accountModel.setIdCustomer(accountRequest.getIdCustomer());

        logger.info("accountModel {}", accountModel);

        return accountRepository.save(accountModel).toMaybe();
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
