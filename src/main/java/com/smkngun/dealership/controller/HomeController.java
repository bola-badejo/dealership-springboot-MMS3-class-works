package com.smkngun.dealership.controller;

import com.smkngun.dealership.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// @Controller returns view NAMES (like "home") that Thymeleaf renders into HTML.
// Compare with @RestController, which would return raw JSON.
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CarService carService;

    // GET / -> showroom homepage with the latest available cars + optional search.
    @GetMapping("/")
    public String home(@RequestParam(required = false) String q, Model model) {
        // model.addAttribute(key, value) puts data in front of the template engine.
        model.addAttribute("cars", carService.search(q));
        model.addAttribute("q", q);
        return "home";                          // Resolves to templates/home.html
    }

    // GET /cars/{id} -> individual car detail page.
    @GetMapping("/cars/{id}")
    public String carDetail(@PathVariable Long id, Model model) {
        model.addAttribute("car", carService.getById(id));
        return "car-detail";
    }

    // After login, Spring doesn't know whether to send ADMIN to /admin or
    // CUSTOMER to /account. This tiny router does the dispatch.
    @GetMapping("/post-login")
    public String postLogin(Authentication auth) {
        boolean admin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return admin ? "redirect:/admin" : "redirect:/account";
    }
}
