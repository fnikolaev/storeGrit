package com.example.store.ServiceUnitTests;

import com.example.store.entity.CartRecord;
import com.example.store.entity.User;
import com.example.store.repository.GoodsRepository;
import com.example.store.repository.OrderGoodsRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.service.CartRecordService;
import com.example.store.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private GoodsRepository goodsRepository;
    @Mock
    private OrderGoodsRepository orderGoodsRepository;
    @Mock
    private CartRecordService cartRecordService;
    @Mock
    private CartRecord cartRecords;

    OrderService orderService;


    @BeforeEach
    public void setUp() {
        orderService = new OrderService(orderRepository, goodsRepository, orderGoodsRepository, cartRecordService,
                cartRecords);
        mockMvc = MockMvcBuilders
                .standaloneSetup(orderService)
                .build();
    }

    @Test
    public void falseCheckAvailable() {
        User user = new User("uncle@mail.ru", "123");

        Mockito.when(cartRecordService.checkAvailable()).thenReturn(false);
// why doesn't work with any(User.class)
        Assertions.assertFalse(orderService.createOrder(user));

        verify(cartRecords, never()).getRecords();
    }

    @Test
    public void fullyCreate() {
        User user = new User("uncle@mail.ru", "123");

        Mockito.when(cartRecordService.checkAvailable()).thenReturn(false);
        Assertions.assertFalse(orderService.createOrder(user));

        verify(cartRecords, never()).getRecords();
    }
}