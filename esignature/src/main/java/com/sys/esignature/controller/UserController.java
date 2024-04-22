package com.sys.esignature.controller;

import com.sys.esignature.entity.User;
import com.sys.esignature.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/selectAll")
    public List<Map<String, Object>> selectAll() {
        List<User> users = userService.selectAll();
        List<Map<String, Object>> usersAll = new ArrayList<>();

        for (User user : users) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("user_id", user.getUserId());
            userMap.put("user_name", user.getUserName());
//
            usersAll.add(userMap);
        }
        return usersAll;
    }
}
