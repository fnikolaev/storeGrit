package com.example.store.controller;

import com.example.store.dto.UserLogRegDTO;
import com.example.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.Map;


@RestController
@Scope(value = "session")
@RequestMapping(value = "/api/")
public class LoginController {
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    public LoginController(DaoAuthenticationProvider daoAuthenticationProvider) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody UserLogRegDTO userLogRegDTO) {
        try{
            final Authentication authenticate = daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userLogRegDTO.getEmail(),
                    userLogRegDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }
        catch(Exception e){
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        Map<String ,Object> response = new HashMap<>();
        response.put("SESSION_ID", RequestContextHolder.currentRequestAttributes().getSessionId());
        return ResponseEntity.ok(response);
    }
}
