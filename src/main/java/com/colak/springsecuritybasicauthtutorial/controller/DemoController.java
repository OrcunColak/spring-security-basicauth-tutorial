package com.colak.springsecuritybasicauthtutorial.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DemoController {

    // http://localhost:8080/api/v1/secured/hello-world
    @GetMapping(value = "/secured/hello-world")
    public String securedCall() {
        return "hello world secured!!";
    }

    // http://localhost:8080/api/v1/hello-world
    @GetMapping(value = "/hello-world")
    public String unsecuredCall() {
        return "hello world!!";
    }

}
