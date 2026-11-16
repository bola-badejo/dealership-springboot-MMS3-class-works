package com.smkngun.dealership.controller;

import com.smkngun.dealership.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ControllerAdvice = cross-cutting handlers that apply to EVERY @Controller.
// Great for turning our domain exceptions into nice error pages.
@ControllerAdvice
public class GlobalExceptionHandler {

    // Any time a controller (or deeper) throws NotFoundException, we land here.
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(NotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error/404";
    }
}
