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
import jakarta.servlet.http.HttpSession;

//dashboard controller
//handles: dashboard rendering, food creation, food logging, and daily total calculation.
@Controller
public class DashboardController {

    // temporary in memory storage for food creations, each food is stored as a map that conatins: name, calories, sugar, protein,
    private List<Map<String, Object>> foods = new ArrayList<>();

    // Temporary in-memory storage for logged food entries. Each log stores calculated nutrient totals for servings.
    private List<Map<String, Object>> logs = new ArrayList<>();

    private double calorieGoal = 2000;
    private double proteinGoal = 150;
    private double sugarGoal = 50;

    //displays the dashbaord
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        // Redirect to login if user is not authenticated
        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }

        // Hardcoded date for development (can later be dynamic)
        model.addAttribute("viewDate", "2026-02-22");

        // Send food and log data to Thymeleaf template
        model.addAttribute("foods", foods);
        model.addAttribute("logs", logs);

        // Calculate daily totals
        double totalCalories = 0;
        double totalProtein = 0;
        double totalSugar = 0;

        for (Map<String, Object> log : logs) {
            totalCalories += (double) log.get("calories");
            totalProtein += (double) log.get("protein");
            totalSugar += (double) log.get("sugar");
        }

        // Add totals to model
        model.addAttribute("calories", totalCalories);
        model.addAttribute("protein", totalProtein);
        model.addAttribute("sugar", totalSugar);

        model.addAttribute("calorieGoal", calorieGoal);
        model.addAttribute("proteinGoal", proteinGoal);
        model.addAttribute("sugarGoal", sugarGoal);

        return "dashboard";
    }

    @GetMapping("/goals")
    public String goals(Model model) {
        model.addAttribute("calorieGoal", 2000);
        model.addAttribute("proteinGoal", 150);
        model.addAttribute("sugarGoal", 50);
        return "goals";
    }

    @PostMapping("/goals")
    public String updateGoals(
            @RequestParam double calorieGoal,
            @RequestParam double proteinGoal,
            @RequestParam double sugarGoal,
            RedirectAttributes ra
    ) {
        this.calorieGoal = calorieGoal;
        this.proteinGoal = proteinGoal;
        this.sugarGoal = sugarGoal;

        ra.addFlashAttribute("msg", "Goals updated successfully");
        return "redirect:/goals";
    }


    //Displays the foods management page.
    @GetMapping("/foods")
    public String foods(Model model) {
        model.addAttribute("foods", foods);
        return "foods";
    }

    //Handles food creation form submission. Adds a new food definition to in-memory storage.
    @PostMapping("/foods")
    public String createFood(@RequestParam String name, @RequestParam double calories, @RequestParam double protein, @RequestParam double sugar, RedirectAttributes ra) {

        // Store food as a Map
        foods.add(Map.of(
                "name", name,
                "calories", calories,
                "protein", protein,
                "sugar", sugar
        ));

        // Flash confirmation message
        ra.addFlashAttribute("msg", "Created food: " + name);

        return "redirect:/dashboard";
    }


    //Handles food logging form submission. Calculates nutrient totals and stores the result in logs list.
    @PostMapping("/logs")
    public String logFood(@RequestParam String foodName, @RequestParam double servings, @RequestParam(required = false) String date, RedirectAttributes ra)
    {
        for (Map<String, Object> food : foods) {
            if (food.get("name").equals(foodName)) {

                double baseCalories = (double) food.get("calories");
                double baseProtein = (double) food.get("protein");
                double baseSugar = (double) food.get("sugar");

                // Store calculated totals for logged serving
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

        // Flash confirmation message
        ra.addFlashAttribute("msg", "Logged " + servings + " serving(s) of " + foodName);
        return "redirect:/dashboard";
    }
}
