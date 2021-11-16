package com.example.store.ServiceUnitTests;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.entity.Goods;
import com.example.store.repository.GoodsRepository;
import com.example.store.service.GoodsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class GoodsServiceTests {
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private GoodsRepository goodsRepository;

    GoodsService goodsService;


    @BeforeEach
    public void setUp(){
        goodsService = new GoodsService(goodsRepository);
        mockMvc = MockMvcBuilders
                .standaloneSetup(goodsService)
                .build();
    }

    @Test
    public void enougthGoods(){
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(1L, 15L);
        Goods goods = new Goods(1L,"charger",18L,240L);

        Mockito.when(goodsRepository.findById(cartAdditionDTO.getId())).thenReturn(Optional.of(goods));

        Assert.assertEquals(true, goodsService.enoughQuantity(cartAdditionDTO));
    }

    @Test
    public void notEnougthGoods(){
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(1L, 20L);
        Goods goods = new Goods(1L,"charger",18L,240L);

        Mockito.when(goodsRepository.findById(cartAdditionDTO.getId())).thenReturn(Optional.of(goods));

        Assert.assertEquals(false, goodsService.enoughQuantity(cartAdditionDTO));
    }
}
