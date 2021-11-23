package com.example.store.ServiceUnitTests;

import com.example.store.dto.UserLogRegDTO;
import com.example.store.entity.User;
import com.example.store.repository.UserRepository;
import com.example.store.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @BeforeEach
    public void setUp(){
        userService = new UserService(userRepository, bCryptPasswordEncoder, daoAuthenticationProvider);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userService)
                .build();
    }

    @Test
    public void findByEmail(){
        String email = "unit@mail.ru";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(new User("unit@mail.ru", "123"));

        Assertions.assertEquals(new User("unit@mail.ru", "123"), userService.findByEmail(email));
    }

    @Test
    public void regEmailExistsInDb(){
        UserLogRegDTO existingUser = new UserLogRegDTO("existing@mail.ru", "123");

        Mockito.when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(new User(existingUser));

        Assertions.assertEquals(ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use")
                , userService.registerUser(existingUser));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void regEmailNotExistsInDb(){
        UserLogRegDTO existingUser = new UserLogRegDTO("existing@mail.ru", "123");

        Mockito.when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(null);

        Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).body("Registered")
                , userService.registerUser(existingUser));
    }

    @Test
    public void encodingTest(){
        User user = new User("existing@mail.ru", "123");
        String passwordBeforeEnc  = user.getPassword();

        when(bCryptPasswordEncoder.encode(any())).thenReturn("$23fwcwce2f3fvs");
        userService.addUser(user);

        ArgumentCaptor<User> requestCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(requestCaptor.capture());
        String passwordAfterEnc = requestCaptor.getValue().getPassword();

        Assertions.assertNotEquals(passwordBeforeEnc, passwordAfterEnc);
    }


}
