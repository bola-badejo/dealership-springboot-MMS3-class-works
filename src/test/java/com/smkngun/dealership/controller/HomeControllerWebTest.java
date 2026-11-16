package com.smkngun.dealership.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Full-slice MVC test: boots Spring but uses MockMvc (no real HTTP server).
// A small test of our Security rules + homepage rendering.
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HomeControllerWebTest {

    @Autowired MockMvc mvc;

    // Disable the DataLoader for tests so we don't try to seed a DB we don't need.
    @TestConfiguration
    static class NoSeedConfig {
        @Bean
        CommandLineRunner noop() { return args -> {}; }
    }

    @Test
    void homepage_isPublic_andRendersTitle() throws Exception {
        mvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("home"))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Find your next drive")));
    }

    @Test
    void adminArea_requiresLogin() throws Exception {
        mvc.perform(get("/admin"))
            .andExpect(status().is3xxRedirection())      // Redirect -> login page.
            .andExpect(redirectedUrl("/login"));
    }
}
