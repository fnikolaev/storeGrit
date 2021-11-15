package com.example.store.ServiceUnitTests;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.entity.CartRecord;
import com.example.store.entity.Goods;
import com.example.store.repository.GoodsRepository;
import com.example.store.service.CartRecordService;
import com.example.store.service.GoodsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartRecordServiceTest {
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CartRecord cartRecords;
    @Mock
    private GoodsService goodsService;
    @Mock
    private GoodsRepository goodsRepository;

    CartRecordService cartRecordService;


    @BeforeEach
    public void setUp(){
        cartRecordService = new CartRecordService(cartRecords, goodsRepository, goodsService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(cartRecordService)
                .build();
    }

    @Test
    public void trueAddRecord(){
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(1L, 15L);

        Mockito.when(goodsService.enoughQuantity(cartAdditionDTO)).thenReturn(true);
        //Mockito.when(cartRecords.getRecords()).thenReturn(Map.of(1L,1L));
        //Mockito.doReturn(false).when(cartRecords).getRecords().containsKey(cartAdditionDTO.getId());
        Assertions.assertTrue(goodsService.enoughQuantity(cartAdditionDTO));

        //verify(cartRecords).getRecords().put(1L, 15L);
    }
}
