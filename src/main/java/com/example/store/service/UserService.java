package com.example.store.service;
import com.example.store.entity.User;
import com.example.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void addUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean userExists(String email){
        if(userRepository.findByEmail(email)!=null){
            System.out.println("try to send 409");
            return true;
        }
        return false;
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }

    public void deleteAllUsers(){
        userRepository.deleteAll();
    }
}
