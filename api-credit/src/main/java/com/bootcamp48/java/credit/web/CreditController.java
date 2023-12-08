package com.bootcamp48.java.credit.web;

import com.bootcamp48.java.credit.config.Constant;
import com.bootcamp48.java.credit.dtos.CreditRequest;
import com.bootcamp48.java.credit.model.CreditModel;
import com.bootcamp48.java.credit.service.ICreditService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(Constant.RESOURCE_GENERIC)
public class CreditController {

    private final ICreditService accountService;

    @GetMapping
    public Flowable<CreditModel> getAll() {
        return this.accountService.findAll();
    }

    @GetMapping(Constant.RESOURCE_IDCUSTOMER)
    public Flowable<CreditModel> getByIdCustomer(@PathVariable String idCustomer) {
        return this.accountService.findByIdCustomer(idCustomer);
    }
    @GetMapping(Constant.RESOURCE_IDPRODUCT)
    public Maybe<CreditModel> getByIdProduct(@PathVariable String idProduct) {
        return this.accountService.findById(idProduct);
    }
    @PostMapping
    public Single<ResponseEntity<CreditModel>> create(@RequestBody CreditRequest creditRequest) throws Exception {
        return this.accountService.create(creditRequest)
                .map(transactionModel -> ResponseEntity.ok().body(transactionModel))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

}
