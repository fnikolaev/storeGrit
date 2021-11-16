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
import static org.mockito.Mockito.*;

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
    public void notEnougthGoodsToAddRecord(){
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(1L, 15L);

        Mockito.when(goodsService.enoughQuantity(cartAdditionDTO)).thenReturn(false);

        Assertions.assertFalse(cartRecordService.addRecord(cartAdditionDTO));

        verify(goodsService).enoughQuantity(cartAdditionDTO);
    }

    //@Test
    public void enougthGoodsToAddRecordNew(){
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(1L, 15L);

        Mockito.when(goodsService.enoughQuantity(cartAdditionDTO)).thenReturn(true);
        Mockito.when(cartRecords.getRecords()).thenReturn(Map.of(2L,2L));

        Assertions.assertTrue(cartRecordService.addRecord(cartAdditionDTO));

        verify(cartRecords,times(2)).getRecords();
        verify(goodsService).enoughQuantity(any(CartAdditionDTO.class));
    }

    //@Test
    public void renougthGoodsToAddRecordNew(){
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(1L, 15L);

        Mockito.when(goodsService.enoughQuantity(cartAdditionDTO)).thenReturn(true);
        Mockito.when(cartRecords.getRecords()).thenReturn(Map.of(1L,2L));
        Mockito.when(goodsService.enoughQuantity(new CartAdditionDTO(cartAdditionDTO.getId(), 15L+2L)))
                .thenReturn(true);


        Assertions.assertTrue(cartRecordService.addRecord(cartAdditionDTO));
        //cartRecordService.addRecord(cartAdditionDTO);
        verify(goodsService,times(2)).enoughQuantity(any(CartAdditionDTO.class));
    }
}
