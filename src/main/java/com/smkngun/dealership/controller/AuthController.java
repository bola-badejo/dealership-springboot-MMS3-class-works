package com.smkngun.dealership.controller;

import com.smkngun.dealership.dto.RegistrationForm;
import com.smkngun.dealership.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

// Handles the custom login + registration pages (Spring Security handles the POST /login).
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";             // templates/login.html
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        // Backing object the form will bind to. Must be in the model for th:object to work.
        model.addAttribute("form", new RegistrationForm());
        return "register";
    }

    // @Valid activates Bean Validation. BindingResult MUST come right after it so
    // Spring puts errors there (otherwise a validation failure throws an exception).
    @PostMapping("/register")
    public String submitRegister(@Valid @ModelAttribute("form") RegistrationForm form,
                                 BindingResult errors,
                                 Model model) {
        if (errors.hasErrors()) {
            return "register";       // Re-render the page with validation messages.
        }
        try {
            userService.registerCustomer(form);
        } catch (IllegalArgumentException dup) {
            // Username/email already taken — show a friendly message, keep input filled.
            model.addAttribute("regError", dup.getMessage());
            return "register";
        }
        // Success -> back to login with a ?registered flag the template can read.
        return "redirect:/login?registered";
    }
}
