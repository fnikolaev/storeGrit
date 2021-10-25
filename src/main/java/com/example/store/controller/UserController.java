package com.example.store.controller;

import com.example.store.entity.User;
import com.example.store.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/newpassword")
    public ResponseEntity changePassword(String newPassword, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        userService.changePassword(user, newPassword);

        return ResponseEntity.ok("k");
    }
}
