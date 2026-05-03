package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

//dashboard controller
//handles: dashboard rendering, food creation, food logging, and daily total calculation.
@Controller
public class DashboardController {

    NutritionService service = new NutritionService();
    DailyLog dailyLog = new DailyLog();

    //displays the dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        //Redirect to login if user is not authenticated
        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }

        //Calculate daily totals, add to model
       LocalDate today = LocalDate.now();

        model.addAttribute("calories", dailyLog.getCaloriesForDay(today));
        model.addAttribute("protein", dailyLog.getProteinsForDay(today));
        model.addAttribute("sugar", dailyLog.getSugarsForDay(today));

        // calculate weekly totals
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        model.addAttribute("weekStart", startOfWeek);
        model.addAttribute("weekEnd", endOfWeek);

        model.addAttribute("weeklyCalories", dailyLog.getCaloriesForRange(startOfWeek, endOfWeek));
        model.addAttribute("weeklyProtein", dailyLog.getProteinsForRange(startOfWeek, endOfWeek));
        model.addAttribute("weeklySugar", dailyLog.getSugarsForRange(startOfWeek, endOfWeek));


        //Send food, log data, and the current date to Thymeleaf template
        model.addAttribute("foods", getFoods());
        model.addAttribute("logs", getLogs());
        model.addAttribute("viewDate", java.time.LocalDate.now().toString());

        model.addAttribute("calorieGoal", 2000);
        model.addAttribute("proteinGoal", 150);
        model.addAttribute("sugarGoal", 50);

        return "dashboard";
    }

    //Displays the foods management page
    @GetMapping("/foods")
    public String foods(Model model, HttpSession session) {

        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }

        model.addAttribute("foods", getFoods());

        return "foods";
    }

    //Handles food creation form submission. Adds a new food definition to in-memory storage
    @PostMapping("/foods")
    public String createFood(
            @RequestParam String name,
            @RequestParam double calories,
            @RequestParam double protein,
            @RequestParam double sugar,
            RedirectAttributes ra) {

        service.addFood(name, calories, protein, sugar);

        ra.addFlashAttribute("msg", "Food added successfully");

        return "redirect:/dashboard";
    }

    @GetMapping("/goals")
    public String goals(Model model, HttpSession session) {

        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }

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
            RedirectAttributes ra) {

        service.addDailyGoal(calorieGoal, proteinGoal, sugarGoal);

        ra.addFlashAttribute("msg", "Goals saved successfully");

        return "redirect:/goals";
    }

    //Handles food logging form submission.
    @PostMapping("/logs")
    public String logFood(
            @RequestParam String foodName,
            @RequestParam double servings,
            RedirectAttributes ra) {

        service.logFood(foodName, servings);

        ra.addFlashAttribute("msg", "Food logged");

        return "redirect:/dashboard";
    }

    //Returns the foods creatred
    private List<FoodLog> getFoods() {

        List<FoodLog> foods = new ArrayList<>();

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "SELECT name, calories, protein, sugar FROM foods";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                foods.add(new FoodLog(
                        rs.getString("name"),
                        1,
                        rs.getDouble("calories"),
                        rs.getDouble("protein"),
                        rs.getDouble("sugar")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return foods;
    }

    //Returns the food log entries
    private List<FoodLog> getLogs() {

        List<FoodLog> logs = new ArrayList<>();

        try (Connection conn = DatabaseManager.connect()) {

            String sql = """
                    SELECT f.name, l.servings,
                    f.calories * l.servings AS calories,
                    f.protein * l.servings AS protein,
                    f.sugar * l.servings AS sugar
                    FROM food_logs l
                    JOIN foods f ON l.food_id = f.id
                    """;

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                logs.add(new FoodLog(
                        rs.getString("name"),
                        rs.getDouble("servings"),
                        rs.getDouble("calories"),
                        rs.getDouble("protein"),
                        rs.getDouble("sugar")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return logs;
    }
}
