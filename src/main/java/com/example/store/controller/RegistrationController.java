package com.example.store.controller;

import com.example.store.dto.UserLogRegDTO;
import com.example.store.entity.User;
import com.example.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Defines url and method for registration process.
 */
@RestController
@RequestMapping(value = "/api/")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Tries to register user. HttpStatus is conflict if user with said email already exists.
     *
     * @param userLogRegDTO DTO containing user's email and password.
     * @return {@link org.springframework.http.HttpEntity} + {@link HttpStatus}.
     */
    @PostMapping("registration")
    public ResponseEntity<String> registration(@Valid @RequestBody UserLogRegDTO userLogRegDTO) {
        return userService.registerUser(userLogRegDTO);
    }
}
