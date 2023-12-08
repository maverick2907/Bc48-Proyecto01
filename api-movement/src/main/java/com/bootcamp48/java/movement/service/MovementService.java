package com.bootcamp48.java.movement.service;

import com.bootcamp48.java.movement.model.MovementModel;
import com.bootcamp48.java.movement.repository.IMovementRepository;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class MovementService implements IMovementService {

    Logger logger = LoggerFactory.getLogger(MovementService.class);

    private final IMovementRepository movementRepository ;

    @Override
    public Flowable<MovementModel> findAll() {
        return  movementRepository.findAll();
    }

    @Override
    public Single<MovementModel> create(MovementModel movement) {
        logger.info("SERVICE: Create service: {}", movement);
        return this.movementRepository.save(movement)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<MovementModel> findByProductId(String productId) {
        logger.info("SERVICE: Get Find By AccountId: {}", productId);
        return this.movementRepository.findByProductId(productId);
    }

}