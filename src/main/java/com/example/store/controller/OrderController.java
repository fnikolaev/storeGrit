package com.example.store.controller;

import com.example.store.dto.UserOrderDTO;
import com.example.store.service.OrderService;
import com.example.store.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Defines urls and methods for orders.
 */
@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    /**
     * Finds all user's orders.
     *
     * @param principal Current user.
     * @return List of user's orders.
     */
    @GetMapping()
    public ResponseEntity<List<UserOrderDTO>> userOrders(Principal principal) {
        List<UserOrderDTO> response = orderService.getOrders(userService.findByEmail(principal.getName()));

        return ResponseEntity.ok(response);
    }

    /**
     * Changes order's status.
     *
     * @param id Order's id.
     * @return @return {@link org.springframework.http.HttpEntity} + {@link HttpStatus}.
     */
    @DeleteMapping("/delete")
    public ResponseEntity deleteOrder(Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("deleted");
    }
}
