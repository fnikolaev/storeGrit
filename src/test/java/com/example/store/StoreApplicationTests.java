package com.example.store;

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
import org.springframework.web.context.request.RequestContextHolder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        goodsService.deleteAllGoods();
        goodsService.addGoods(new Goods("pen", 10,25));
        goodsService.addGoods(new Goods("charger", 32,240));

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
        System.out.println(RequestContextHolder.currentRequestAttributes().getSessionId());

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
                .andExpect(jsonPath("$[1].price").value(240));
    }

    @Test()
    @WithMockUser(username = "busymail@mail.ru",password = "123")
    public void teststh() throws Exception {

        this.mockMvc.perform(get("/api/goods"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("pen"))
                .andExpect(jsonPath("$[0].available").value(10))
                .andExpect(jsonPath("$[0].price").value(25))
                .andExpect(jsonPath("$[1].title").value("charger"))
                .andExpect(jsonPath("$[1].available").value(32))
                .andExpect(jsonPath("$[1].price").value(240));
    }

}
