package com.example.store.service;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.entity.CartRecord;
import com.example.store.entity.Goods;
import com.example.store.entity.User;
import com.example.store.repository.CartRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartRecordService {
    private final CartRecordRepository cartRecordRepository;

    @Autowired
    public CartRecordService(CartRecordRepository cartRecordRepository) {
        this.cartRecordRepository = cartRecordRepository;
    }

    public List<CartRecord> getUsersCartRecords(User user){
        return cartRecordRepository.findCartRecordsByUser(user);
    }

    public void addCartRecord(CartAdditionDTO cartAdditionDTO, User user, Goods goods){
        cartRecordRepository.save(new CartRecord(user, goods, cartAdditionDTO.getQuantity()));
    }
}
