package com.example.store.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "goods")
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private int available;
    private int price;

    public Goods(String title, int available, int price) {
        this.id = null;
        this.title = title;
        this.available = available;
        this.price = price;
    }

    public Goods() {
    }
}
