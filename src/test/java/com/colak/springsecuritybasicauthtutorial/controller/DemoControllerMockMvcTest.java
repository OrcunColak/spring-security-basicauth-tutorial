package com.colak.springsecuritybasicauthtutorial.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
// Ask MockMvc to be injected by using the @AutoConfigureMockMvc
@AutoConfigureMockMvc
class DemoControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void whenAdminTriesToAccess() throws Exception {
        String url = "/api/v1/secured/hello-world";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user")
    void whenUserTriesToAccess_thenForbidden() throws Exception {
        String url = "/api/v1/secured/hello-world";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
