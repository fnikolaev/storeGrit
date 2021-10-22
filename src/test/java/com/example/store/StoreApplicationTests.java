package com.example.store;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.dto.UserLogRegDTO;
import com.example.store.entity.Goods;
import com.example.store.entity.User;
import com.example.store.service.GoodsService;
import com.example.store.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class StoreApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;



    @BeforeAll
    public void initDb(){
        userService.deleteAllUsers();
        userService.addUser(new User("busymail@mail.ru", "123"));
        userService.addUser(new User("cartuser@mail.ru", "123"));
        userService.addUser(new User("deleteuser@mail.ru", "123"));

        goodsService.deleteAllGoods();
        goodsService.addGoods(new Goods("pen", 10L,25L));
        goodsService.addGoods(new Goods("charger", 32L,240L));
        goodsService.addGoods(new Goods("cup", 40L,50L));
    }

    @Test()
    public void testRegNotExisting() throws Exception {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("freemail@mail.ru", "123");

        this.mockMvc.perform(post("/api/registration")
                    .content(objectMapper.writeValueAsString(userLogRegDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test()
    public void testRegExisting() throws Exception {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("busymail@mail.ru", "123");

        this.mockMvc.perform(post("/api/registration")
                        .content(objectMapper.writeValueAsString(userLogRegDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test()
    public void testCorrectLogin() throws Exception {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("busymail@mail.ru", "123");

        this.mockMvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(userLogRegDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test()
    public void testIncorrectLogin() throws Exception {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("busymail@mail.ru", "wrong_password");

        this.mockMvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(userLogRegDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test()
    @WithMockUser(username = "busymail@mail.ru",password = "123")
    public void testGetGoods() throws Exception {

        this.mockMvc.perform(get("/api/goods"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("pen"))
                .andExpect(jsonPath("$[0].available").value(10))
                .andExpect(jsonPath("$[0].price").value(25))
                .andExpect(jsonPath("$[1].title").value("charger"))
                .andExpect(jsonPath("$[1].available").value(32))
                .andExpect(jsonPath("$[1].price").value(240))
                .andExpect(jsonPath("$[2].title").value("cup"))
                .andExpect(jsonPath("$[2].available").value(40))
                .andExpect(jsonPath("$[2].price").value(50));
    }

    @Test()
    @WithMockUser(username = "busymail@mail.ru",password = "123")
    public void testAddToCart() throws Exception {
        long chargerId = goodsService.goodsByTitle("charger").getId();
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(chargerId, 2L);

        this.mockMvc.perform(post("/api/cart/add")
                .content(objectMapper.writeValueAsString(cartAdditionDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test()
    @WithMockUser(username = "busymail@mail.ru",password = "123")
    public void testNotEnoughGoods() throws Exception {
        long chargerId = goodsService.goodsByTitle("charger").getId();
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(chargerId, 100L);

        this.mockMvc.perform(post("/api/cart/add")
                        .content(objectMapper.writeValueAsString(cartAdditionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test()
    @WithMockUser(username = "cartuser@mail.ru",password = "123")
    public void testCartView() throws Exception {
        long chargerId = goodsService.goodsByTitle("cup").getId();
        long penId = goodsService.goodsByTitle("pen").getId();
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(chargerId, 2L);
        CartAdditionDTO cartAdditionDTO1 = new CartAdditionDTO(penId, 1L);

        this.mockMvc.perform(post("/api/cart/add")
                        .content(objectMapper.writeValueAsString(cartAdditionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(post("/api/cart/add")
                        .content(objectMapper.writeValueAsString(cartAdditionDTO1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(get("/api/cart"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("Sum").value(125));
    }

    @Test()
    @WithMockUser(username = "deleteuser@mail.ru",password = "123")
    public void deleteFromCart() throws Exception {
        long chargerId = goodsService.goodsByTitle("cup").getId();
        long penId = goodsService.goodsByTitle("pen").getId();
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(chargerId, 1L);
        CartAdditionDTO cartAdditionDTO1 = new CartAdditionDTO(penId, 1L);

        this.mockMvc.perform(post("/api/cart/add")
                        .content(objectMapper.writeValueAsString(cartAdditionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(post("/api/cart/add")
                        .content(objectMapper.writeValueAsString(cartAdditionDTO1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(delete("/api/cart/delete?title=pen")
                        .content(objectMapper.writeValueAsString(cartAdditionDTO1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(get("/api/cart"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("Sum").value(50));
    }
}
