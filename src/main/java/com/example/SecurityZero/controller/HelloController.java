package com.example.SecurityZero.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/api/oauth")
    public String hello(){
        return "Hello World";
    }
}
