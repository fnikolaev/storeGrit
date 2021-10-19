package com.example.store.controller;

import com.example.store.entity.Goods;
import com.example.store.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/")
public class StoreController {
    private final GoodsService goodsService;

    @Autowired
    public StoreController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping("goods")
    public ResponseEntity allGoods() {
        List<Goods> goods = goodsService.getAllStoresGoods();
        return ResponseEntity.ok(goodsService.responseGoodsList(goods));
    }
}
