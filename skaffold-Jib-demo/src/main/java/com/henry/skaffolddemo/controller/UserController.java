package com.henry.skaffolddemo.controller;


import com.henry.skaffolddemo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/hello")
    public String hello(){
        return "hello world";
    }

    @GetMapping("/user")
    public User helloUser(){
        var user = new User();
        user.setFirstName("Henry");
        user.setLastName("Xiloj");

        return user;
    }
}
