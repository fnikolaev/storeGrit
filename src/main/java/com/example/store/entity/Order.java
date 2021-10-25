package com.example.store.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private Long total;
    private boolean status;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @OneToMany(mappedBy = "order")
    List<OrderGoods> orderGoods;

    public Order(Date date, Long total, boolean status, User user) {
        this.date = date;
        this.total = total;
        this.status = status;
        this.user = user;
    }

    public Order() {

    }
}
