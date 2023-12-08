package com.bootcamp48.java.transaction.web;

import com.bootcamp48.java.transaction.config.Constant;
import com.bootcamp48.java.transaction.dtos.TransactionRequest;
import com.bootcamp48.java.transaction.model.TransactionModel;
import com.bootcamp48.java.transaction.service.ITransactionService;
import io.reactivex.rxjava3.core.Maybe;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(Constant.RESOURCE_GENERIC)
public class TransactionController {

    private final ITransactionService transactionService;

    @PostMapping
    public Maybe<ResponseEntity<TransactionModel>> transaction(@RequestBody TransactionRequest transactionRequest) {
        return this.transactionService.add(transactionRequest)
                .map(transactionModel -> ResponseEntity.ok().body(transactionModel))
                .switchIfEmpty(Maybe.defer(() ->
                        Maybe.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                ));
    }

}
