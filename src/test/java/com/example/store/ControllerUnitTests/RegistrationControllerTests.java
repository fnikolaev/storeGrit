package com.example.store.ControllerUnitTests;

import com.example.store.controller.RegistrationController;
import com.example.store.dto.UserLogRegDTO;
import com.example.store.entity.User;
import com.example.store.service.UserService;
import com.example.store.service.security.MyUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTests {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private RegistrationController registrationController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp(){
        registrationController = new RegistrationController(userService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(registrationController)
                .build();
    }

    @Test
    public void successfulRegistration() throws Exception {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("unit@mail.ru", "123");

        Mockito.when(userService.userExists("unit@mail.ru")).thenReturn(false);

        mockMvc.perform(
                        post("/api/registration")
                                .content(objectMapper.writeValueAsString(userLogRegDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(userService).userExists(matches("unit@mail.ru"));

        verify(userService).addUser(any(User.class));
    }

    @Test
    public void conflictRegistration() throws Exception {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("unit@mail.ru", "123");

        Mockito.when(userService.userExists(any())).thenReturn(true);

        mockMvc.perform(
                        post("/api/registration")
                                .content(objectMapper.writeValueAsString(userLogRegDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());

        verify(userService).userExists(matches("unit@mail.ru"));

        verify(userService,never()).addUser(any(User.class));
    }
}
