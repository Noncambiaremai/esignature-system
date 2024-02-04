package com.sys.esignature.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")

public class Hello {
    @GetMapping("/index")
    public String index() {
        System.out.println("hhhhhhhhhhhhhhhhhh");
        return "hhhhh";
    }
}
