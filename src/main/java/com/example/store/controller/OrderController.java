package com.example.store.controller;

import com.example.store.dto.UserOrderDTO;
import com.example.store.service.OrderService;
import com.example.store.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserOrderDTO>> userOrders(Principal principal) {
        List<UserOrderDTO> response = orderService.getOrders(userService.findByEmail(principal.getName()));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteOrder(Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("deleted");
    }
}
