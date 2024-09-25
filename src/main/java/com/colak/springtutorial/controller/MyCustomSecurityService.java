package com.colak.springtutorial.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyCustomSecurityService {

    // here you can write your logic on what basis user can get access of this method
    public boolean isResourceOwner(Authentication authentication, String resourceId) {
        log.info("isResourceOwner called with resourceId : {}", resourceId);
        // Your logic to determine if the user is the owner of the resource
        // You might compare the authenticated user's details with the resourceId
        // Return true if the user is the owner; otherwise, return false
        return authentication.getName().equals(resourceId);
    }
}
