package com.colak.springsecuritybasicauthtutorial.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void whenAdminTriesToAccess() {
        String url = "/api/v1/secured/hello-world";
        ResponseEntity<String> responseEntity = testRestTemplate
                .withBasicAuth("admin", "password")
                .getForEntity(url, String.class);

        Assertions.assertEquals("hello world secured!!", responseEntity.getBody());
    }

    @Test
    void whenAdminTriesToAccessByEmail() {
        String url = "/api/v1/secured/hello-world";
        ResponseEntity<String> responseEntity = testRestTemplate
                .withBasicAuth("orcun@example.com", "password")
                .getForEntity(url, String.class);

        Assertions.assertEquals("hello world secured!!", responseEntity.getBody());
    }

    @Test
    void whenUserTriesToAccess_thenForbidden() {
        String url = "/api/v1/secured/hello-world";
        ResponseEntity<String> responseEntity = testRestTemplate
                .withBasicAuth("user", "password")
                .getForEntity(url, String.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void whenUserTriesToAccess_CustomRule() {
        String url = "/api/v1/secured/hello-world/user";
        ResponseEntity<String> responseEntity = testRestTemplate
                .withBasicAuth("user", "password")
                .getForEntity(url, String.class);

        Assertions.assertEquals("hello world secured!! for user", responseEntity.getBody());
    }
}
