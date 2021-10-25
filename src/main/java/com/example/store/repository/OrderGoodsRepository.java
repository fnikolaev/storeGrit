package com.example.store.repository;

import com.example.store.entity.Order;
import com.example.store.entity.OrderGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderGoodsRepository extends JpaRepository<OrderGoods, Long> {
    List<OrderGoods> findByOrder(Order order);
}
