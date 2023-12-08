package com.bootcamp48.java.account.web;

import com.bootcamp48.java.account.config.Constant;
import com.bootcamp48.java.account.dtos.AccountRequest;
import com.bootcamp48.java.account.model.AccountModel;
import com.bootcamp48.java.account.service.IAccountService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping(Constant.RESOURCE_GENERIC)
public class AccountController {

    private final IAccountService accountService;

    @GetMapping
    public Flowable<AccountModel> getAll() {
        return this.accountService.findAll();
    }

    @GetMapping(Constant.RESOURCE_IDCUSTOMER)
    public Flowable<AccountModel> getByIdCustomer(@PathVariable String idCustomer) {
        return this.accountService.findByIdCustomer(idCustomer);
    }

    @GetMapping(Constant.RESOURCE_IDACCOUNT)
    public Maybe<AccountModel> getByIdAccount(@PathVariable String idAccount) {
        return this.accountService.findById(idAccount);
    }
    @PostMapping
    public Single<ResponseEntity<AccountModel>> create(@RequestBody AccountRequest accountRequest) throws Exception {
        return this.accountService.create(accountRequest)
                .map(transactionModel -> ResponseEntity.ok().body(transactionModel))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

}
