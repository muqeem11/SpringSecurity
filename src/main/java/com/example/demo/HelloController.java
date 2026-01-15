package com.example.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hello")
    public String sayHello(){
        return "Hello World!";
     }
     @GetMapping("/admin/hello")
     public  String sayAdminHello(){
        return "hello, Admin";
     }
    @GetMapping("/user/hello")
    public  String sayUserHello(){
        return "hello, User";
    }
}
