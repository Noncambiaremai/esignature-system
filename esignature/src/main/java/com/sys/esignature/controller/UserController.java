package com.sys.esignature.controller;

import com.sys.esignature.entity.User;
import com.sys.esignature.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

//    @GetMapping("/getUser")
//    public List<User> getUser(@RequestParam("userId") String userId, @RequestParam("userPassword") String userPassword) {
//        System.out.println("controller");
//        return userService.getUser(userId, userPassword);
//    }



    // 在这里定义处理 HTTP 请求的方法
//    @GetMapping
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/{id}")
//    public User getUserById(@PathVariable Long id) {
//        return userService.getUserById(id);
//    }

    @PostMapping("/login")
    public List<User> login(@RequestParam("userId") String userId, @RequestParam("userPassword") String userPassword) {
        return userService.login(userId, userPassword);
    }

    @GetMapping("/isAccountExists")
    public boolean isAccountExists(@RequestParam("user_id") String userId) {
        return userService.isAccountExists(userId);
    }

    @PostMapping("/register")
    public void register(
            @RequestParam("userId") String userId,
            @RequestParam("userPassword") String userPassword,
            @RequestParam("faceImage") String faceImage,
            @RequestParam("faceFeature") String faceFeature
    ) {


    }
}
