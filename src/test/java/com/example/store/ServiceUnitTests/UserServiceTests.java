package com.example.store.ServiceUnitTests;

import com.example.store.dto.UserLogRegDTO;
import com.example.store.entity.User;
import com.example.store.repository.UserRepository;
import com.example.store.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void setUp(){
        userService = new UserService(userRepository, bCryptPasswordEncoder);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userService)
                .build();
    }

    @Test
    public void userNotExists() throws Exception {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("unit@mail.ru", "123");

        Mockito.when(userRepository.findByEmail("unit@mail.ru")).thenReturn(null);

        Assert.assertEquals(false, userService.userExists("unit@mail.ru"));
    }

    @Test
    public void userExists() throws Exception {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("unit@mail.ru", "123");

        Mockito.when(userRepository.findByEmail("unit@mail.ru")).thenReturn(new User(userLogRegDTO));

        Assert.assertEquals(true, userService.userExists("unit@mail.ru"));

        //verify(userRepository).findByEmail("unit@mail.ru");
    }

    @Test
    public void findByEmail() throws Exception {
        String email = "unit@mail.ru";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(new User("unit@mail.ru", "123"));

        Assert.assertEquals(new User("unit@mail.ru", "123"), userService.findByEmail(email));
    }
}
