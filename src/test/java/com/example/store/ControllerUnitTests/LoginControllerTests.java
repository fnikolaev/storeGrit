package com.example.store.ControllerUnitTests;

import com.example.store.controller.RegistrationController;
import com.example.store.dto.UserLogRegDTO;
import com.example.store.entity.User;
import com.example.store.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTests {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private RegistrationController registrationController;

    @Mock
    private UserService userService;

    @Mock
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @BeforeEach
    public void setUp(){
        registrationController = new RegistrationController(userService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(registrationController)
                .build();
    }

    @Test
    public void okLogin() throws Exception {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("existinh@mail.ru", "123");
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        //Mockito.when(SecurityContextHolder.getContext().setAuthentication(authentication)).then(throw Error);
        //Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);


        mockMvc.perform(
                        post("/api/login")
                                .content(objectMapper.writeValueAsString(userLogRegDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void badLogin() throws Exception {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("notexisting@mail.ru", "123");

        Mockito.when(userService.userExists("unit@mail.ru")).thenReturn(false);

        mockMvc.perform(
                        post("/api/login")
                                .content(objectMapper.writeValueAsString(userLogRegDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}
