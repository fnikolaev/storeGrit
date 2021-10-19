package com.example.store.controller;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.dto.CartResponseDTO;
import com.example.store.entity.CartRecord;
import com.example.store.entity.Goods;
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
        int sum=0;
        for(CartRecord record : cartRecords){
            response.put(record.getOrdinal(), new CartResponseDTO(record.getGoods().getTitle(), record.getQuantity()));
            sum += record.getQuantity()*record.getGoods().getPrice();
        }
        response.put("Sum", sum);

        return ResponseEntity.ok(response);
    }

    @PostMapping("cart/add")
    public ResponseEntity addToCart(@RequestBody CartAdditionDTO cartAdditionDTO, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if(goodsService.enoughQuantity(cartAdditionDTO)){
            int numOfRecords = cartRecordService.getUsersCartRecords(user).size();
            goodsService.decreaseAvailable(cartAdditionDTO);
            cartRecordService.addCartRecord(cartAdditionDTO, user, goodsService.goodsByID(cartAdditionDTO.getId()),numOfRecords+1);

            return ResponseEntity.ok("goods added to cart");
        }
        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("cart/delete")
    public ResponseEntity deleteCartRecord(String title, Principal principal){
        User user = userService.findByEmail(principal.getName());
        Goods goods = goodsService.goodsByTitle(title);
        CartRecord forDelete = cartRecordService.getRecordByUserGoods(user, goods);
        cartRecordService.deleteRecord(forDelete);

        return ResponseEntity.ok("deleted from cart");
    }

    @PatchMapping("cart/update")
    public ResponseEntity updateCartRecord(@RequestBody CartAdditionDTO cartAdditionDTO, Principal principal){
        User user = userService.findByEmail(principal.getName());
        CartRecord cartRecord = cartRecordService.getRecordByUserAndOrdinal(user, Math.toIntExact(cartAdditionDTO.getId()));
        Goods goods = cartRecord.getGoods();
        int difference = cartAdditionDTO.getQuantity()-cartRecord.getQuantity();

        if(cartAdditionDTO.getQuantity()>cartRecord.getQuantity()){
            if(goodsService.enoughQuantityInt(goods.getId(), difference)){
                goodsService.modifyGoodsAvailable(goods, difference);
                cartRecordService.modifyQuantity(cartRecord, difference);
            }else
                return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }else{
            difference*=-1;
            goodsService.modifyGoodsAvailable(goods, difference);
            cartRecordService.modifyQuantity(cartRecord, difference);
        }
        return ResponseEntity.ok("cart updated");
    }
}
