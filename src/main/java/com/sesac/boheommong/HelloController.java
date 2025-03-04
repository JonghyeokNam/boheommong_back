package com.sesac.boheommong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Swagger!";
    }

    @GetMapping
    public String main() {
        return "main page";
    }
}
