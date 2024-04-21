package com.sys.esignature.service;

import com.sys.esignature.entity.User;
import com.sys.esignature.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    public List<User> getUser(String userId, String userPassword) {
//        System.out.println("service");
//        return userRepository.getUser(userId, userPassword);
//    }


//    public User getUserById(Long id) {
//        return userRepository.findById(id).orElse(null);
//    }
//

    public boolean isAccountExists(String userId) {
        return userRepository.isAccountExists(userId);
    }

    public List<User> login(String userId, String userPassword) {
        return userRepository.login(userId, userPassword);
    }
}
