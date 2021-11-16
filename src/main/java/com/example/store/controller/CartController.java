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
import java.security.Principal;
import java.util.Map;

/**
 * Defines urls and methods for login process.
 */
@RestController
@Validated
@RequestMapping(value = "/api/cart")
public class CartController {
    private final UserService userService;
    private final GoodsService goodsService;
    private final OrderService orderService;

    private final CartRecordService cartRecordService;

    public CartController(UserService userService, GoodsService goodsService, OrderService orderService,
                          CartRecordService cartRecordService) {
        this.userService = userService;
        this.goodsService = goodsService;
        this.orderService = orderService;
        this.cartRecordService = cartRecordService;
    }

    /**
     * Finds goods which in user's cart at current session
     *
     * @return All goods in user's cart + total sum.
     */
    @GetMapping()
    public ResponseEntity<Map<Object, Object>> usersCart() {
        CartDTO response = cartRecordService.allRecords();
        return ResponseEntity.ok(response.getCart());
    }

    /**
     * Adds goods to user's cart
     *
     * @param cartAdditionDTO DTO containing id of goods and quantity.
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@Valid @RequestBody CartAdditionDTO cartAdditionDTO) {
        if (cartRecordService.addRecord(cartAdditionDTO)) {
            return ResponseEntity.ok("goods added to cart");
        }
        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }

    /**
     * Delete goods from user's cart.
     *
     * @param title Title of goods.
     * @return {@link org.springframework.http.HttpEntity} + {@link HttpStatus}.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCartRecord(String title) {
        cartRecordService.deleteRecord(goodsService.goodsByTitle(title).getId());
        return ResponseEntity.ok("deleted from cart");
    }

    /**
     * Changes goods quantity in user's cart.
     *
     * @param cartAdditionDTO DTO containing id of goods and quantity.
     * @return {@link org.springframework.http.HttpEntity} + {@link HttpStatus}.
     */
    @PatchMapping("/update")
    public ResponseEntity<String> updateCartRecord(@Valid @RequestBody CartAdditionDTO cartAdditionDTO) {
        return cartRecordService.updateRecord(cartAdditionDTO);
    }

    /**
     * Makes checkout.
     *
     * @param principal Current user.
     * @return {@link org.springframework.http.HttpEntity} + {@link HttpStatus}.
     */
    @PostMapping("/order")
    public ResponseEntity<String> createOrder(Principal principal) {
        if(orderService.createOrder(userService.findByEmail(principal.getName()))){
            return ResponseEntity.ok("Order created");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough goods in store");
    }
}
