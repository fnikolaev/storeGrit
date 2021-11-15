package com.example.store.controller;

import com.example.store.entity.Goods;
import com.example.store.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Defines url and GET method for goods.
 */
@RestController
@RequestMapping(value = "/api/")
public class StoreController {
    private final GoodsService goodsService;

    @Autowired
    public StoreController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    /**
     * Returns all products in the store
     *
     * @return {@link org.springframework.http.HttpEntity} + {@link HttpStatus}.
     */
    @GetMapping("goods")
    public ResponseEntity allGoods() {
        List<Goods> goods = goodsService.getAllStoresGoods();
        return ResponseEntity.ok(goodsService.responseGoodsList(goods));
    }
}
