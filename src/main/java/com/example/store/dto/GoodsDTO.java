package com.example.store.dto;

import com.example.store.entity.Goods;
import lombok.Data;

/**
 * Describes goods in store.
 */
@Data
public class GoodsDTO {
    private Long id;
    private String title;
    private Long available;
    private Long price;

    public GoodsDTO(Goods goods) {
        this.id = goods.getId();
        this.title = goods.getTitle();
        this.available = goods.getAvailable();
        this.price = goods.getPrice();
    }
}
