package com.example.store.service;
import com.example.store.dto.UserLogRegDTO;
import com.example.store.entity.User;
import com.example.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains methods for user's managing.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                       DaoAuthenticationProvider daoAuthenticationProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }


    /**
     * Register new user if there is no user with same email in DB.
     *
     * @param user  User from registration controller.
     * @return OK if registration succeed.
     */
    public ResponseEntity<String> registerUser(UserLogRegDTO user){
        if(userRepository.findByEmail(user.getEmail()) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use");
        }
        addUser(new User(user));
        return ResponseEntity.status(HttpStatus.OK).body("Registered");
    }

    /**
     * Encodes password and saves in DB.
     *
     * @param user New user.
     */
    public void addUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Tries to authenticate user.
     *
     * @param userLogRegDTO DTO with email and password
     * @return If correct dto then returns session id, otherwise - bad request.
     */
    public ResponseEntity<Map<String ,String>> loginUser(UserLogRegDTO userLogRegDTO){
        try{
            final Authentication authenticate = daoAuthenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(userLogRegDTO.getEmail(), userLogRegDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }
        catch(Exception e){
            return new ResponseEntity("Email or password are incorrect", HttpStatus.BAD_REQUEST);
        }
        Map<String ,String> response = new HashMap<>();
        response.put("SESSION_ID", RequestContextHolder.currentRequestAttributes().getSessionId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void deleteAllUsers(){
        userRepository.deleteAll();
    }
}
