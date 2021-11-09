package com.example.store.service;

import com.example.store.dto.UserOrderDTO;
import com.example.store.entity.*;
import com.example.store.repository.GoodsRepository;
import com.example.store.repository.OrderGoodsRepository;
import com.example.store.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final GoodsRepository goodsRepository;
    private final OrderGoodsRepository orderGoodsRepository;
    private final CartRecordService cartRecordService;
    private final CartRecord cartRecords;

    public OrderService(OrderRepository orderRepository, GoodsRepository goodsRepository, OrderGoodsRepository orderGoodsRepository, CartRecordService cartRecordService, CartRecord cartRecords) {
        this.orderRepository = orderRepository;
        this.goodsRepository = goodsRepository;
        this.orderGoodsRepository = orderGoodsRepository;
        this.cartRecordService = cartRecordService;
        this.cartRecords = cartRecords;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public boolean createOrder(User user) {
        if (!cartRecordService.checkAvailable()) {
            return false;
        }

        log.error("*********************************************8888888**********************************************");

//        if (goodsRepository.findById(2L).get().getAvailable() < 32) {
//            return false;
//        }

        if (goodsRepository.getById(2L).getAvailable() < 32) {
            return false;
        }

        final Map<Long, Long> records = cartRecords.getRecords();
        Long sum = 0L;
        for (Long id : records.keySet()) {
            Goods goods = goodsRepository.findById(id).get();
            sum += goods.getPrice() * records.get(id);
        }
        Date dateNow = new Date();
        Order order = new Order(dateNow, sum, true, user);
        orderRepository.save(order);

        Order lastUsersOrder = orderRepository.findByUserOrderByIdDesc(user).get(0);
        for (Long id : records.keySet()) {
            Goods goods = goodsRepository.findById(id).get();
            goods.setAvailable(goods.getAvailable() - records.get(id));
            goodsRepository.save(goods);

            orderGoodsRepository.save(new OrderGoods(goods, lastUsersOrder, records.get(id)));
        }
        return true;
    }

    public List<UserOrderDTO> getOrders(User user) {
        List<Order> orders = orderRepository.findByUserOrderByIdDesc(user);
        List<UserOrderDTO> userOrderDTOS = new ArrayList<>();
        for (Order order : orders) {
            userOrderDTOS.add(new UserOrderDTO(order.getId(), order.getDate(), order.getTotal(), order.isStatus()));
        }
        return userOrderDTOS;
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.getById(id);
        List<OrderGoods> orderGoods = orderGoodsRepository.findByOrder(order);
        for (OrderGoods orGoods : orderGoods) {
            Goods goods = orGoods.getGoods();
            goods.setAvailable(goods.getAvailable() + orGoods.getQuantity());
            goodsRepository.save(goods);
        }
        order.setStatus(false);
        orderRepository.save(order);
    }
}
