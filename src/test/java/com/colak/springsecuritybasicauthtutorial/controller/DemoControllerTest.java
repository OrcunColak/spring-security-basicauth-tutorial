package com.colak.springsecuritybasicauthtutorial.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testSecuredCall() {
        String url = "/api/v1/secured/hello-world";
        ResponseEntity<String> responseEntity = testRestTemplate
                .withBasicAuth("user", "password")
                .getForEntity(url, String.class);

        Assertions.assertEquals("hello world secured!!", responseEntity.getBody());
    }

}
