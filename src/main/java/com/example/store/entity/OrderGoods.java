package com.example.store.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "orders_goods")
public class OrderGoods implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "goods_id")
    private Goods goods;
    @ManyToOne()
    @JoinColumn(name = "order_id")
    private Order order;
    private Long quantity;

    public OrderGoods(Goods goods, Order order, Long quantity) {
        this.goods = goods;
        this.order = order;
        this.quantity = quantity;
    }
}
