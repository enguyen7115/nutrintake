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
        model.addAttribute("calories", dailyLog.getCalories());
        model.addAttribute("protein", dailyLog.getProteins());
        model.addAttribute("sugar", dailyLog.getSugars());
        model.addAttribute("carbs", dailyLog.getCarbs());
        model.addAttribute("fats", dailyLog.getFats());
        model.addAttribute("sodium", dailyLog.getSodium());
        model.addAttribute("cholesterol", dailyLog.getCholesterol());

        //Send food, log data, and the current date to Thymeleaf template
        model.addAttribute("foods", getFoods());
        model.addAttribute("logs", getLogs());
        model.addAttribute("viewDate", java.time.LocalDate.now().toString());

        model.addAttribute("calorieGoal", model.getAttribute("calorieGoal"));
        model.addAttribute("proteinGoal", model.getAttribute("proteinGoal"));
        model.addAttribute("sugarGoal", model.getAttribute("sugarGoal"));
        model.addAttribute("carbsGoal",model.getAttribute("carbsGoal"));
        model.addAttribute("fatsGoal", model.getAttribute("fatsGoal"));
        model.addAttribute("sodiumGoal", model.getAttribute("sodiumGoal"));
        model.addAttribute("cholesterolGoal", model.getAttribute("cholesterolGoal"));

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
            @RequestParam double carbs,
            @RequestParam double fats,
            @RequestParam double sodium,
            @RequestParam double cholesterol,
            RedirectAttributes ra) {

        service.addFood(name, calories, protein, sugar, carbs, fats, sodium,cholesterol);

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
            @RequestParam double carbsGoal,
            @RequestParam double fatsGoal,
            @RequestParam double sodiumGoal,
            @RequestParam double cholesterolGoal,
            RedirectAttributes ra) {

        service.addDailyGoal(calorieGoal, proteinGoal, sugarGoal, carbsGoal, fatsGoal, sodiumGoal, cholesterolGoal);

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

            String sql = "SELECT name, calories, protein, sugar, carbs, fats, sodium, cholesterol FROM foods";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                foods.add(new FoodLog(
                        rs.getString("name"),
                        1,
                        rs.getDouble("calories"),
                        rs.getDouble("protein"),
                        rs.getDouble("sugar"),
                        rs.getDouble("carbs"),
                        rs.getDouble("fats"),
                        rs.getDouble("sodium"),
                        rs.getDouble("cholesterol")
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
                    f.sugar * l.servings AS sugar,
                    f.carbs * l.servings AS carbs,
                    f.fats * l.servings AS fats,
                    f.sodium * l.servings AS sodium,    
                    f.cholesterol * l.servings AS cholesterol    
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
                        rs.getDouble("sugar"),
                        rs.getDouble("carbs"),
                        rs.getDouble("fats"),
                        rs.getDouble("sodium"),
                        rs.getDouble("cholesterol")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return logs;
    }
}
