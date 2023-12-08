package com.bootcamp48.java.client.web;

import com.bootcamp48.java.client.dtos.ClientRequest;
import com.bootcamp48.java.client.model.ClientModel;
import com.bootcamp48.java.client.config.Constant;
import com.bootcamp48.java.client.service.IClientService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(Constant.RESOURCE_GENERIC)
public class ClientController {

    private final IClientService clientService;

    @GetMapping
    public Flowable<ClientModel> getAllCustomers() {
        return this.clientService.getAll()
                .observeOn(Schedulers.io());
    }

    @GetMapping(Constant.RESOURCE_CUSTOMER)
    public Maybe<ClientModel> getById(@PathVariable String idCustomer) {
        return this.clientService.getById(idCustomer)
                .observeOn(Schedulers.io());
    }

    @PostMapping
    public Single<ResponseEntity<ClientModel>> create(@RequestBody ClientRequest clientRequest) {
        return this.clientService.add(clientRequest)
                .map(clientModel -> ResponseEntity.ok().body(clientModel))
                .onErrorReturnItem(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }


}
