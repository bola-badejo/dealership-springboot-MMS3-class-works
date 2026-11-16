package com.smkngun.dealership.controller;

import com.smkngun.dealership.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// The customer-facing "my account" area. Protected by SecurityConfig.
@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SaleService saleService;

    // @AuthenticationPrincipal gives us the currently-logged-in user directly —
    // no need to dig through SecurityContextHolder.
    @GetMapping("/account")
    public String myAccount(@AuthenticationPrincipal UserDetails me, Model model) {
        model.addAttribute("username", me.getUsername());
        model.addAttribute("purchases", saleService.listForCustomer(me.getUsername()));
        return "account";
    }

    // POST /buy/{carId} -> record the sale for the current user.
    @PostMapping("/buy/{carId}")
    public String buy(@PathVariable Long carId,
                      @AuthenticationPrincipal UserDetails me,
                      RedirectAttributes flash) {
        try {
            saleService.purchase(carId, me.getUsername());
            flash.addFlashAttribute("msg", "Purchase successful. Congratulations!");
            return "redirect:/account";
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("err", ex.getMessage());
            return "redirect:/cars/" + carId;
        }
    }
}
