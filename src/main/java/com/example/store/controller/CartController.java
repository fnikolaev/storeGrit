package com.example.store.controller;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.dto.CartResponse;
import com.example.store.entity.CartRecord;
import com.example.store.entity.User;
import com.example.store.service.CartRecordService;
import com.example.store.service.GoodsService;
import com.example.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/")
public class CartController {
    private final CartRecordService cartRecordService;
    private final UserService userService;
    private final GoodsService goodsService;

    @Autowired
    public CartController(CartRecordService cartRecordService, UserService userService, GoodsService goodsService) {
        this.cartRecordService = cartRecordService;
        this.userService = userService;
        this.goodsService= goodsService;
    }

    @GetMapping("cart")
    public ResponseEntity usersCart(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<CartRecord> cartRecords = cartRecordService.getUsersCartRecords(user);
        Map<Object, Object> response = new HashMap<>();
        Integer ordinal=1;
        int sum=0;
        for(CartRecord record : cartRecords){
            response.put(ordinal, new CartResponse(record.getGoods().getTitle(), record.getQuantity()));
            sum += record.getQuantity()*record.getGoods().getPrice();
            ordinal++;
        }
        response.put("Sum", sum);

        return ResponseEntity.ok(response);
    }

    @PostMapping("cart/add")
    public ResponseEntity addToCart(@RequestBody CartAdditionDTO cartAdditionDTO, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if(goodsService.enoughQuantity(cartAdditionDTO)){
            goodsService.decreaseAvailable(cartAdditionDTO);
            cartRecordService.addCartRecord(cartAdditionDTO, user, goodsService.goodsByID(cartAdditionDTO.getId()));

            return ResponseEntity.ok("goods added to cart");
        }

        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }
}
