package com.henry.dockerfiledemo.controller;


import com.henry.dockerfiledemo.record.UserRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/hello")
    public String hello(){
        return "hello world";
    }

    @GetMapping("/user")
    public UserRecord helloUser(){
       return new UserRecord("Henry", "Xiloj");

    }
}
