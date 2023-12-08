package com.bootcamp48.java.movement.service;

import com.bootcamp48.java.movement.model.MovementModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface IMovementService {

    Flowable<MovementModel> findAll() ;
    Single<MovementModel> create(MovementModel movement);
    Flowable<MovementModel> findByProductId(String productId);

}
