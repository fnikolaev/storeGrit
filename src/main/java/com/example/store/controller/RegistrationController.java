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


@RestController
@RequestMapping(value = "/api/")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("registration")
    public ResponseEntity registration(@Valid @RequestBody UserLogRegDTO userLogRegDTO) {
        if (userService.userExists(userLogRegDTO.getEmail())){
            return new ResponseEntity(null, HttpStatus.CONFLICT);
        }
        userService.addUser(new User(userLogRegDTO));
        return ResponseEntity.ok("added");
    }
}
