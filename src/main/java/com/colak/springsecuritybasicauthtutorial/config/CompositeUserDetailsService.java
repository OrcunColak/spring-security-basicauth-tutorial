package com.colak.springsecuritybasicauthtutorial.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;

public class CompositeUserDetailsService implements UserDetailsService {

    private List<UserDetailsManager> userDetailsServiceList;

    @Autowired
    public void setUserDetailsServiceList(List<UserDetailsManager> userDetailsServiceList) {
        this.userDetailsServiceList = userDetailsServiceList;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (UserDetailsService userDetailsService : userDetailsServiceList) {
            try {
                // Attempt to load user details from the current UserDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // If user details are found, return them
                return userDetails;
            } catch (UsernameNotFoundException ignored) {
                // If user is not found in the current UserDetailsService, move on to the next one
            }
        }
        // If no UserDetailsService provides user details, throw an exception
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
