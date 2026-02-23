package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("viewDate", "2026-02-22");
        model.addAttribute("calories", 1200);
        model.addAttribute("protein", 60);
        model.addAttribute("sugar", 20);
        model.addAttribute("logs", java.util.List.of());
        model.addAttribute("logs", java.util.List.of(
                java.util.Map.of("foodName", "Oatmeal", "servings", 1, "calories", 300),
                java.util.Map.of("foodName", "Chicken Bowl", "servings", 1.5, "calories", 650)
        ));
        model.addAttribute("foods", java.util.List.of("Oatmeal", "Chicken Bowl", "Protein Bar"));

        return "dashboard";
    }
}
