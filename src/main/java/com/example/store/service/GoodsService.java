package com.example.store.service;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.dto.GoodsDTO;
import com.example.store.entity.Goods;
import com.example.store.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public Goods goodsByTitle(String title){
        return goodsRepository.findByTitle(title);
    }

    public List<Goods> getAllStoresGoods(){
        return goodsRepository.findAll();
    }

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

    public boolean enoughQuantity(CartAdditionDTO cartAdditionDTO){
        Long available = goodsRepository.getById(cartAdditionDTO.getId()).getAvailable();
        return available >= cartAdditionDTO.getQuantity();
    }

    public boolean enoughQuantityLong(Long goodsId, Long quantity){
        Long available = goodsRepository.getById(goodsId).getAvailable();
        return available >= quantity;
    }

    public void decreaseAvailable(CartAdditionDTO cartAdditionDTO){
        Goods goods = goodsRepository.getById(cartAdditionDTO.getId());
        goods.setAvailable(goods.getAvailable() - cartAdditionDTO.getQuantity());
        goodsRepository.save(goods);
    }

    public void modifyGoodsAvailable(Goods goods, Long difference){
        goods.setAvailable(goods.getAvailable() - difference);
        goodsRepository.save(goods);
    }
}
