package com.example.store.repository;

import com.example.store.entity.Order;
import com.example.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    ArrayList<Order> findByUserOrderByIdDesc(User user);
    ArrayList<Order> findAllByOrderByIdDesc();
}