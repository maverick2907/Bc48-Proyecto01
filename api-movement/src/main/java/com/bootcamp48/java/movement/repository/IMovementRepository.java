package com.bootcamp48.java.movement.repository;

import com.bootcamp48.java.movement.model.MovementModel;
import io.reactivex.rxjava3.core.Flowable;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface IMovementRepository extends RxJava3CrudRepository<MovementModel, String> {
    Flowable<MovementModel> findByProductId(String productId);

}
