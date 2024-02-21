package com.sys.esignature.service;

import com.sys.esignature.entity.User;
import com.sys.esignature.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<User> getUser(User user) {
        return userRepository.getUser(user);
    }
}
