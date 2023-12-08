package com.bootcamp48.java.client.repository;

import com.bootcamp48.java.client.model.ClientModel;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClientRepository extends RxJava3CrudRepository<ClientModel, String> {

}
