package com.example.store.service;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.dto.CartDTO;
import com.example.store.entity.CartRecord;
import com.example.store.entity.Goods;
import com.example.store.repository.GoodsRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CartRecordService {
    private final CartRecord cartRecords;
    private final GoodsRepository goodsRepository;
    private final GoodsService goodsService;


    public CartRecordService(CartRecord cartRecord, GoodsRepository goodsRepository, GoodsService goodsService) {
        this.cartRecords = cartRecord;
        this.goodsRepository = goodsRepository;
        this.goodsService = goodsService;
    }

    public CartDTO allRecords() {
        final Map<Long, Long> records = cartRecords.getRecords();
        CartDTO cartDTO = new CartDTO();
        Long sum = 0L;
        Long ordinal = 1L;

        if (records.keySet().size() == 0) {
            cartDTO.getCart().put("Cart is empty", null);
            return cartDTO;
        }

        for (Long id : records.keySet()) {
            if (goodsRepository.findById(id).isPresent()) {
                Goods goods = goodsRepository.findById(id).get();

                cartDTO.getCart().put(ordinal, Map.of("title", goods.getTitle(),
                        "quantity", records.get(id)));
                ordinal++;
                sum += goods.getPrice() * records.get(id);
            }
        }
        cartDTO.getCart().put("Sum", sum);
        return cartDTO;
    }

    public boolean addRecord(CartAdditionDTO cartAdditionDTO) {
        if(!goodsService.enoughQuantity(cartAdditionDTO)){
            return false;
        }
        if (cartRecords.getRecords().containsKey(cartAdditionDTO.getId())) {
            Long newQuantity = cartRecords.getRecords().get(cartAdditionDTO.getId()) + cartAdditionDTO.getQuantity();
            if(goodsService.enoughQuantity(new CartAdditionDTO(cartAdditionDTO.getId(), newQuantity))){
                cartRecords.getRecords().put(cartAdditionDTO.getId(), newQuantity);
                return true;
            }
            return false;
        }
        cartRecords.getRecords().put(cartAdditionDTO.getId(), cartAdditionDTO.getQuantity());
        return true;
    }

    public void deleteRecord(Long id){
        cartRecords.getRecords().remove(id);
    }

    public boolean updateRecord(CartAdditionDTO cartAdditionDTO){
        if(goodsService.enoughQuantity(cartAdditionDTO)){
            cartRecords.getRecords().put(cartAdditionDTO.getId(), cartAdditionDTO.getQuantity());
            return true;
        }
        return false;
    }

    public boolean checkAvailable(){
        final Map<Long, Long> records = cartRecords.getRecords();
        for(Long id : records.keySet()){
            if(!goodsService.enoughQuantity(new CartAdditionDTO(id, records.get(id)))){
                return false;
            }
        }
        return true;
    }
}
