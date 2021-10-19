package com.example.store.service;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.entity.Goods;
import com.example.store.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    private final GoodsRepository goodsRepository;

    @Autowired
    public GoodsService(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    public Goods goodsByID(Long id){
        return goodsRepository.getById(id);
    }

    public List<Goods> getAllStoresGoods(){
        return goodsRepository.findAll();
    }

    public void deleteAllGoods(){
        goodsRepository.deleteAll();
    }

    public void addGoods(Goods goods){
        goodsRepository.save(goods);
    }

    public boolean enoughQuantity(CartAdditionDTO cartAdditionDTO){
        int available = goodsRepository.getById(cartAdditionDTO.getId()).getAvailable();
        if(available < cartAdditionDTO.getQuantity())
            return false;
        return true;
    }

    public void decreaseAvailable(CartAdditionDTO cartAdditionDTO){
        Goods goods = goodsRepository.getById(cartAdditionDTO.getId());
        goods.setAvailable(goods.getAvailable() - cartAdditionDTO.getQuantity());
        goodsRepository.save(goods);
    }
}
