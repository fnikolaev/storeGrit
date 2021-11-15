package com.example.store.service;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.dto.CartDTO;
import com.example.store.entity.CartRecord;
import com.example.store.entity.Goods;
import com.example.store.repository.GoodsRepository;
import org.springframework.stereotype.Service;


import java.util.Map;

/**
 *  Contains methods for managing user's session cart.
 */
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

    /**
     *
     * @return All goods from cart + total sum.
     */
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

    /**
     * If there is enough goods in store, then changes quantity in cart.
     *
     * @param cartAdditionDTO DTO for changing quantity in cart by id.
     * @return <code>true</code> if quantity completely changed, <code>false</code> otherwise.
     */
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

    /**
     * Delete record from cart.
     *
     * @param id Id of cart record.
     */
    public void deleteRecord(Long id){
        cartRecords.getRecords().remove(id);
    }

    /**
     *
     * @param cartAdditionDTO Provides new quantity for cart record.
     * @return <code>true</code> if enough quantity for updating, <code>false</code> otherwise.
     */
    public boolean updateRecord(CartAdditionDTO cartAdditionDTO){
        if(goodsService.enoughQuantity(cartAdditionDTO)){
            cartRecords.getRecords().put(cartAdditionDTO.getId(), cartAdditionDTO.getQuantity());
            return true;
        }
        return false;
    }

    /**
     * Checks if there is enough goods in store.
     *
     * @return <code>true</code> if enough quantity, <code>false</code> otherwise.
     */
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
