package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class DashboardController {

    private List<Map<String, Object>> foods = new ArrayList<>();
    private List<Map<String, Object>> logs = new ArrayList<>();


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("viewDate", "2026-02-23");

        model.addAttribute("foods", foods);
        model.addAttribute("logs", logs);

        double totalCalories = 0;
        double totalProtein = 0;
        double totalSugar = 0;

        for (Map<String, Object> log : logs) {
            totalCalories += (double) log.get("calories");
            totalProtein += (double) log.get("protein");
            totalSugar += (double) log.get("sugar");
        }

        model.addAttribute("calories", totalCalories);
        model.addAttribute("protein", totalProtein);
        model.addAttribute("sugar", totalSugar);

        return "dashboard";
    }

    @PostMapping("/foods")
    public String createFood(
            @RequestParam String name,
            @RequestParam double calories,
            @RequestParam double protein,
            @RequestParam double sugar,
            RedirectAttributes ra
    ) {

        foods.add(Map.of(
                "name", name,
                "calories", calories,
                "protein", protein,
                "sugar", sugar
        ));

        ra.addFlashAttribute("msg", "Created food: " + name);

        return "redirect:/dashboard";
    }


    @PostMapping("/logs")
    public String logFood(
            @RequestParam String foodName,
            @RequestParam double servings,
            @RequestParam(required = false) String date,
            RedirectAttributes ra
    ) {
        for (Map<String, Object> food : foods) {
            if (food.get("name").equals(foodName)) {

                double baseCalories = (double) food.get("calories");
                double baseProtein = (double) food.get("protein");
                double baseSugar = (double) food.get("sugar");

                logs.add(Map.of(
                        "foodName", foodName,
                        "servings", servings,
                        "calories", baseCalories * servings,
                        "protein", baseProtein * servings,
                        "sugar", baseSugar * servings
                ));

                break;
            }
        }
        ra.addFlashAttribute("msg", "Logged " + servings + " serving(s) of " + foodName);
        return "redirect:/dashboard";
    }
}
