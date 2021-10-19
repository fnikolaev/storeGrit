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

    public void addCartRecord(CartAdditionDTO cartAdditionDTO, User user, Goods goods, int ordinal){
        cartRecordRepository.save(new CartRecord(user, goods, cartAdditionDTO.getQuantity(), ordinal));
    }

    public void deleteAllRecords(){
        cartRecordRepository.deleteAll();
    }

    public CartRecord getRecordByUserGoods(User user, Goods goods){
        return cartRecordRepository.findCartRecordsByUserAndGoods(user,goods);
    }

    public CartRecord getRecordByUserAndOrdinal(User user, int ordinal){
        return cartRecordRepository.findCartRecordByUserAndOrdinal(user, ordinal);
    }

    public void deleteRecord(CartRecord cartRecord){
        cartRecordRepository.delete(cartRecord);
    }

    public void modifyQuantity(CartRecord cartRecord, int difference){
        cartRecord.setQuantity(cartRecord.getQuantity()+difference);
        cartRecordRepository.save(cartRecord);
    }
}
