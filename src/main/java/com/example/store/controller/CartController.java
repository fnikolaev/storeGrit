package com.example.store.controller;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.dto.CartDTO;
import com.example.store.service.CartRecordService;
import com.example.store.service.GoodsService;
import com.example.store.service.OrderService;
import com.example.store.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.Map;

@RestController
@Validated
@RequestMapping(value = "/api/cart")
public class CartController {
    private final UserService userService;
    private final GoodsService goodsService;
    private final OrderService orderService;

    private final CartRecordService cartRecordService;

    public CartController(UserService userService, GoodsService goodsService, OrderService orderService, CartRecordService cartRecordService) {
        this.userService = userService;
        this.goodsService = goodsService;
        this.orderService = orderService;
        this.cartRecordService = cartRecordService;
    }

    @GetMapping()
    public ResponseEntity<Map<Object, Object>> usersCart() {

        CartDTO response = cartRecordService.allRecords();

        return ResponseEntity.ok(response.getCart());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@Valid @RequestBody CartAdditionDTO cartAdditionDTO) {

        if (cartRecordService.addRecord(cartAdditionDTO)) {
            return ResponseEntity.ok("goods added to cart");
        }
        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteCartRecord(String title) {
        cartRecordService.deleteRecord(goodsService.goodsByTitle(title).getId());
        return ResponseEntity.ok("deleted from cart");
    }

    @PatchMapping("/update")
    public ResponseEntity updateCartRecord(@Valid @RequestBody CartAdditionDTO cartAdditionDTO) {
        if(cartRecordService.updateRecord(cartAdditionDTO)){
            return ResponseEntity.ok("cart updated");
        }
        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/order")
    public ResponseEntity createOrder(Principal principal) {
        if(orderService.createOrder(userService.findByEmail(principal.getName()))){
            return ResponseEntity.ok("order created");
        }
        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }
}
