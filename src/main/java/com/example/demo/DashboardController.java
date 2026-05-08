package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.time.DayOfWeek;

//dashboard controller
//handles: dashboard rendering, food creation, food logging, and daily total calculation.
@Controller
public class DashboardController {

    NutritionService service = new NutritionService();
    DailyLog dailyLog = new DailyLog();
    WeeklyLog weeklyLog = new WeeklyLog();

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

        //Send food, log data, and the current date to Thymeleaf template
        model.addAttribute("foods", getFoods());
        model.addAttribute("logs", getLogs());
        model.addAttribute("viewDate", java.time.LocalDate.now().toString());

        // Send week start and end dates to template
        model.addAttribute("weekStart", java.time.LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)));
        model.addAttribute("weekEnd", java.time.LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)));

        // Send weekly totals and add to model
        model.addAttribute("weeklyCalories", weeklyLog.getCalories());
        model.addAttribute("weeklyProtein", weeklyLog.getProteins());
        model.addAttribute("weeklySugar", weeklyLog.getSugars());

        System.out.printf("Weekly Total: %f, %f, %f\n", weeklyLog.getCalories(), weeklyLog.getProteins(), weeklyLog.getSugars());

        double dailyCalorieGoal = 0.0;
        double dailyProteinGoal = 0.0;
        double dailySugarGoal = 0.0;

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "SELECT calories, proteins, sugars FROM daily_goals ORDER BY id DESC LIMIT 1";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dailyCalorieGoal = rs.getDouble("calories");
                dailyProteinGoal = rs.getDouble("proteins");
                dailySugarGoal = rs.getDouble("sugars");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("calorieGoal", dailyCalorieGoal);
        model.addAttribute("proteinGoal", dailyProteinGoal);
        model.addAttribute("sugarGoal", dailySugarGoal);

        double weeklyCalorieGoal = 0.0;
        double weeklyProteinGoal = 0.0;
        double weeklySugarGoal = 0.0;

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "SELECT calories, proteins, sugars FROM weekly_goals ORDER BY id DESC LIMIT 1";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                weeklyCalorieGoal = rs.getDouble("calories");
                weeklyProteinGoal = rs.getDouble("proteins");
                weeklySugarGoal = rs.getDouble("sugars");

                System.out.printf("Weekly Goal: %f, %f, %f\n", weeklyCalorieGoal, weeklyProteinGoal, weeklySugarGoal);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("weeklyCaloriesGoal", weeklyCalorieGoal);
        model.addAttribute("weeklyProteinGoal", weeklyProteinGoal);
        model.addAttribute("weeklySugarGoal", weeklySugarGoal);

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
    public String updateGoals(@RequestParam String goalType,
            @RequestParam double calorieGoal,
            @RequestParam double proteinGoal,
            @RequestParam double sugarGoal,
            RedirectAttributes ra) {

        if (goalType.equals("daily")) {
            service.addDailyGoal(calorieGoal, proteinGoal, sugarGoal);
        } else if (goalType.equals("weekly")) {
            service.addWeeklyGoal(calorieGoal, proteinGoal, sugarGoal);
        }

        ra.addFlashAttribute("msg", "Goals saved successfully");

        return "redirect:/goals";
    }

    @DeleteMapping("/goals")
    public String deleteGoals(@RequestParam String type, RedirectAttributes ra) {
        if (type.equals("daily")) {
            service.deleteDaily();
            ra.addFlashAttribute("msg", "Daily goals deleted successfully");
        } else if (type.equals("weekly")) {
            service.deleteWeekly();
            ra.addFlashAttribute("msg", "Weekly goals deleted successfully");
        }

        return "redirect:/dashboard";
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
