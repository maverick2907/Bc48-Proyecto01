package com.bootcamp48.java.movement.web;

import com.bootcamp48.java.movement.config.Constant;
import com.bootcamp48.java.movement.model.MovementModel;
import com.bootcamp48.java.movement.service.IMovementService;
import io.reactivex.rxjava3.core.Flowable;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping(Constant.RESOURCE_GENERIC)
public class MovementController {

    private final IMovementService movementService;

    @GetMapping
    public Flowable<MovementModel> getAll(){
        return this.movementService.findAll();
    }

    @GetMapping(Constant.RESOURCE_PRODUCT)
    public Flowable<MovementModel> findByProductId(@PathVariable String productId) {
        return this.movementService.findByProductId(productId);
    }

}
