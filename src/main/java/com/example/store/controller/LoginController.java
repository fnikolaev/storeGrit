package com.example.store.controller;

import com.example.store.dto.UserLogRegDTO;
import com.example.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * Defines url and method for login process.
 */
@RestController
@RequestMapping(value = "/api/")
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Tries to authenticate user.
     *
     * @param userLogRegDTO DTO containing user's email and password.
     * @return {@link org.springframework.http.HttpEntity} + {@link HttpStatus}.
     */
    @PostMapping("login")
    public ResponseEntity<Map<String,String>> login(@Valid @RequestBody UserLogRegDTO userLogRegDTO) {
        return userService.loginUser(userLogRegDTO);
    }
}
