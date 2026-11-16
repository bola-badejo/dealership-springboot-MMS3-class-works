package com.smkngun.dealership.controller;

import com.smkngun.dealership.dto.CarForm;
import com.smkngun.dealership.mapper.CarMapper;
import com.smkngun.dealership.service.CarService;
import com.smkngun.dealership.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Everything in here is admin-only — enforced by SecurityConfig (/admin/**).
@Controller
@RequestMapping("/admin")           // Every endpoint below is prefixed with /admin.
@RequiredArgsConstructor
public class AdminController {

    private final CarService carService;
    private final SaleService saleService;
    private final CarMapper carMapper;

    // GET /admin -> dashboard with headline stats.
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("availableCount", carService.countAvailable());
        model.addAttribute("soldCount", carService.countSold());
        model.addAttribute("salesCount", saleService.totalSalesCount());
        model.addAttribute("revenue", saleService.totalRevenue());
        model.addAttribute("recentSales", saleService.listAll().stream().limit(5).toList());
        return "admin/dashboard";
    }

    // ---------- Car management ----------

    @GetMapping("/cars")
    public String carList(Model model) {
        model.addAttribute("cars", carService.listAll());
        return "admin/cars";
    }

    // Shows an empty form for creating a brand-new car.
    @GetMapping("/cars/new")
    public String newCarForm(Model model) {
        model.addAttribute("form", new CarForm());
        model.addAttribute("mode", "create");
        return "admin/car-form";
    }

    // Shows the form pre-filled from the DB for editing.
    @GetMapping("/cars/{id}/edit")
    public String editCarForm(@PathVariable Long id, Model model) {
        model.addAttribute("form", carMapper.toForm(carService.getmodel(id)));
        model.addAttribute("mode", "edit");
        return "admin/car-form";
    }

    // One handler for both create and update. If form.id is null -> create, else -> update.
    @PostMapping("/cars/save")
    public String saveCar(@Valid @ModelAttribute("form") CarForm form,
                          BindingResult errors,
                          Model model,
                          RedirectAttributes flash) {
        if (errors.hasErrors()) {
            model.addAttribute("mode", form.getId() == null ? "create" : "edit");
            return "admin/car-form";
        }
        if (form.getId() == null) {
            carService.create(form);
            flash.addFlashAttribute("msg", "Car added to inventory.");
        } else {
            carService.update(form.getId(), form);
            flash.addFlashAttribute("msg", "Car updated.");
        }
        return "redirect:/admin/cars";
    }

    // POST (not GET) for delete — GET must be side-effect-free per HTTP rules.
    @PostMapping("/cars/{id}/delete")
    public String deleteCar(@PathVariable Long id, RedirectAttributes flash) {
        carService.delete(id);
        flash.addFlashAttribute("msg", "Car removed.");
        return "redirect:/admin/cars";
    }

    // ---------- Sales ----------

    @GetMapping("/sales")
    public String sales(Model model) {
        model.addAttribute("sales", saleService.listAll());
        model.addAttribute("revenue", saleService.totalRevenue());
        return "admin/sales";
    }
}
