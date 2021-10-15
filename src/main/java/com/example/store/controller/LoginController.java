package com.example.store.controller;

import com.example.store.dto.UserLogRegDTO;
import com.example.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/")
public class LoginController {
    private UserService userService;
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    public LoginController(UserService userService, DaoAuthenticationProvider daoAuthenticationProvider) {
        this.userService = userService;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody UserLogRegDTO userLogRegDTO) {
        try{
            daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userLogRegDTO.getEmail(),userLogRegDTO.getPassword()));
        }
        catch(Exception e){
            return ResponseEntity.ok("not session");
        }

        Map<String ,Object> response = new HashMap<>();
        //response.put("SESSION_ID", userService.getSessionId());
        return ResponseEntity.ok("session");

    }
}
