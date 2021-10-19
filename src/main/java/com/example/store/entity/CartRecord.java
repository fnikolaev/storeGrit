package com.example.store.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "cartrecords")
public class CartRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    private int quantity;
    private int ordinal;

    public CartRecord(User user, Goods goods, int quantity) {
        this.user = user;
        this.goods = goods;
        this.quantity = quantity;
    }

    public CartRecord(User user, Goods goods, int quantity, int ordinal) {
        this.user = user;
        this.goods = goods;
        this.quantity = quantity;
        this.ordinal = ordinal;
    }

    public CartRecord() {
    }

}
