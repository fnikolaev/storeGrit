package com.example.store.service;

import com.example.store.dto.UserOrderDTO;
import com.example.store.entity.*;
import com.example.store.repository.GoodsRepository;
import com.example.store.repository.OrderGoodsRepository;
import com.example.store.repository.OrderRepository;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Contains methods for user's orders.
 */
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final GoodsRepository goodsRepository;
    private final OrderGoodsRepository orderGoodsRepository;
    private final CartRecordService cartRecordService;
    private final CartRecord cartRecords;

    public OrderService(OrderRepository orderRepository, GoodsRepository goodsRepository,
                        OrderGoodsRepository orderGoodsRepository, CartRecordService cartRecordService,
                        CartRecord cartRecords) {
        this.orderRepository = orderRepository;
        this.goodsRepository = goodsRepository;
        this.orderGoodsRepository = orderGoodsRepository;
        this.cartRecordService = cartRecordService;
        this.cartRecords = cartRecords;
    }

    /**
     * Makes checkout for current user.
     *
     * @param user Current user.
     * @return <code>true</code> if order has been created, <code>false</code> otherwise.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public boolean createOrder(User user) {
        if (!cartRecordService.checkAvailable()) {
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

        for (Long id : records.keySet()) {
            Goods goods = goodsRepository.findById(id).get();
            goods.setAvailable(goods.getAvailable() - records.get(id));
            goodsRepository.save(goods);

            orderGoodsRepository.save(new OrderGoods(goods, order, records.get(id)));
        }
        System.out.println();
        System.out.println("create service finished");
        System.out.println();
        return true;
    }

    /**
     *
     * @param user Current user.
     * @return All user's orders.
     */
    public List<UserOrderDTO> getOrders(User user) {
        List<Order> orders = orderRepository.findByUserOrderByIdDesc(user);
        List<UserOrderDTO> userOrderDTOS = new ArrayList<>();
        for (Order order : orders) {
            userOrderDTOS.add(new UserOrderDTO(order.getId(), order.getDate(), order.getTotal(), order.isStatus()));
        }
        return userOrderDTOS;
    }

    /**
     * Changes order's status, returns all goods to store.
     *
     * @param id Order's id.
     */
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).get();
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
