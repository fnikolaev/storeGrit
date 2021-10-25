package com.example.store.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "goods")
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Long available;
    private Long price;
    @OneToMany(mappedBy = "goods")
    List<OrderGoods> orderGoods;


    public Goods(String title, Long available, Long price) {
        this.id = null;
        this.title = title;
        this.available = available;
        this.price = price;
    }

    public Goods() {
    }
}
