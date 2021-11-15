package com.example.store.service;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.dto.GoodsDTO;
import com.example.store.entity.Goods;
import com.example.store.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *  Contains methods for managing goods in store.
 */
@Service
public class GoodsService {
    private final GoodsRepository goodsRepository;

    @Autowired
    public GoodsService(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    public Goods goodsByTitle(String title){
        return goodsRepository.findByTitle(title);
    }

    public List<Goods> getAllStoresGoods(){
        return goodsRepository.findAll();
    }

    /**
     * Wraps goods List in DTO List.
     *
     * @param goodsList All goods from store.
     * @return List of goods in certain DTO.
     */
    public List<GoodsDTO> responseGoodsList(List<Goods> goodsList){
        List<GoodsDTO> goodsDTOS = new ArrayList<>();
        for(Goods goods : goodsList){
            goodsDTOS.add(new GoodsDTO(goods));
        }
        return goodsDTOS;
    }

    public void deleteAllGoods(){
        goodsRepository.deleteAll();
    }

    public void addGoods(Goods goods){
        goodsRepository.save(goods);
    }

    /**
     * Checks if it is enough certain goods in store.
     *
     * @param cartAdditionDTO DTO trying to update goods quantity in cart.
     * @return <code>true</code> if quantity completely changed, <code>false</code> otherwise.
     */
    public boolean enoughQuantity(CartAdditionDTO cartAdditionDTO){
        Long available = goodsRepository.findById(cartAdditionDTO.getId()).get().getAvailable();
        return available >= cartAdditionDTO.getQuantity();
    }
}
