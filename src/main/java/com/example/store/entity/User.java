package com.example.store.entity;

import com.example.store.dto.UserLogRegDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    public User(UserLogRegDTO userLogRegDTO) {
        this.id = null;
        this.email = userLogRegDTO.getEmail();
        this.password = userLogRegDTO.getPassword();
    }

    public User(String email, String password) {
        this.id = null;
        this.email = email;
        this.password = password;
    }

    public User() {
    }
}
