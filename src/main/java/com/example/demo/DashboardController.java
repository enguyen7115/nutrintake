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
        model.addAttribute("fats", dailyLog.getFats());
        model.addAttribute("saturated_fat", dailyLog.getSaturated_fat());
        model.addAttribute("trans_fat", dailyLog.getTrans_fat());
        model.addAttribute("cholesterol", dailyLog.getCholesterol());
        model.addAttribute("sodium", dailyLog.getSodium());
        model.addAttribute("carbs", dailyLog.getCarbs());
        model.addAttribute("fiber", dailyLog.getFiber());
        model.addAttribute("sugars", dailyLog.getSugars());
        model.addAttribute("protein", dailyLog.getProteins());

        //Send food, log data, and the current date to Thymeleaf template
        model.addAttribute("foods", getFoods());
        model.addAttribute("logs", getLogs());
        model.addAttribute("viewDate", java.time.LocalDate.now().toString());

        model.addAttribute("calorieGoal", model.getAttribute("calorieGoal"));
        model.addAttribute("fatsGoal", model.getAttribute("fatsGoal"));
        model.addAttribute("cholesterolGoal", model.getAttribute("cholesterolGoal"));
        model.addAttribute("sodiumGoal", model.getAttribute("sodiumGoal"));
        model.addAttribute("carbsGoal",model.getAttribute("carbsGoal"));
        model.addAttribute("proteinGoal", model.getAttribute("proteinGoal"));

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

            @RequestParam double carbs,
                @RequestParam double fiber,
                @RequestParam double sugars,

            @RequestParam double fats,
                @RequestParam double saturated_fat,
                @RequestParam double trans_fat,

            @RequestParam double sodium,
            @RequestParam double cholesterol,
            RedirectAttributes ra) {

        service.addFood(name, calories, fats, saturated_fat, trans_fat, cholesterol, sodium, carbs, fiber,
                sugars, protein);

        ra.addFlashAttribute("msg", "Food added successfully");

        return "redirect:/dashboard";
    }

    @GetMapping("/goals")
    public String goals(Model model, HttpSession session) {

        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }

        model.addAttribute("calorieGoal", 0);
        model.addAttribute("fatsGoal (g)", 0);
        model.addAttribute("cholesterolGoal (mg)", 0);
        model.addAttribute("sodiumGoal (mg)", 0);
        model.addAttribute("carbsGoal (g)", 0);
        model.addAttribute("proteinGoal (g)", 0);

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

        service.addDailyGoal(calorieGoal, fatsGoal, cholesterolGoal, sodiumGoal, carbsGoal, proteinGoal);

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

            String sql = "SELECT name, calories, fats, saturated_fat, trans_fat, cholesterol, sodium, carbs, fiber, sugars, protein" +
                    "FROM foods";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                foods.add(new FoodLog(
                        rs.getString("name"),
                        1,
                        rs.getDouble("calories"),
                        rs.getDouble("fats"),
                        rs.getDouble("saturated_fat"),
                        rs.getDouble("trans_fat"),
                        rs.getDouble("cholesterol"),
                        rs.getDouble("sodium"),
                        rs.getDouble("carbs"),
                        rs.getDouble("fiber"),
                        rs.getDouble("sugars"),
                        rs.getDouble("protein")
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
                    f.fats * l.servings AS fats,
                    f.saturated_fat * l.servings AS saturated_fat,
                    f.trans_fat * l.servings AS trans_fat,
                    f.cholesterol * l.servings AS cholesterol
                    f.sodium * l.servings AS sodium,
                    f.carbs * l.servings AS carbs,
                    f.fiber * l.servings AS fiber,    
                    f.sugars * l.servings AS sugars,        
                    f.protein * l.servings AS protein,    
                        
                    FROM food_logs l
                    JOIN foods f ON l.food_id = f.id
                    """;

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                logs.add(new FoodLog(
                        rs.getString("name"),
                        rs.getDouble("Servings"),
                        rs.getDouble("calories"),
                        rs.getDouble("fats"),
                        rs.getDouble("saturated_fat"),
                        rs.getDouble("trans_fat"),
                        rs.getDouble("cholesterol"),
                        rs.getDouble("sodium"),
                        rs.getDouble("carbs"),
                        rs.getDouble("fiber"),
                        rs.getDouble("sugars"),
                        rs.getDouble("protein")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return logs;
    }
}
