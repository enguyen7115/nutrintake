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
        model.addAttribute("fats", dailyLog.getFats());
        model.addAttribute("saturated_fat", dailyLog.getSaturated_fat());
        model.addAttribute("trans_fat", dailyLog.getTrans_fat());
        model.addAttribute("cholesterol", dailyLog.getCholesterol());
        model.addAttribute("sodium", dailyLog.getSodium());
        model.addAttribute("carbs", dailyLog.getCarbs());
        model.addAttribute("fiber", dailyLog.getFiber());
        model.addAttribute("sugar", dailyLog.getSugars());
        model.addAttribute("protein", dailyLog.getProteins());

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
        model.addAttribute("weeklyFats", weeklyLog.getFats());
        model.addAttribute("weeklyCholesterol", weeklyLog.getCholesterol());
        model.addAttribute("weeklyFiber", weeklyLog.getFiber());
        model.addAttribute("weeklyCarbs", weeklyLog.getCarbs());
        model.addAttribute("weeklySodium", weeklyLog.getSodium());
        model.addAttribute("weeklySaturatedFat", weeklyLog.getSaturated_fat());
        model.addAttribute("weeklyTransFat", weeklyLog.getTrans_fat());

        double dailyCalorieGoal = 0.0;
        double dailyProteinGoal = 0.0;
        double dailyCarbsGoal = 0.0;
        double dailyCholesterolGoal = 0.0;
        double dailyFatsGoal = 0.0;
        double dailySodiumGoal = 0.0;

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "SELECT calories, fats, cholesterol, sodium, carbs, proteins FROM daily_goals ORDER BY id DESC LIMIT 1";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dailyCalorieGoal = rs.getDouble("calories");
                dailyProteinGoal = rs.getDouble("proteins");
                dailyCarbsGoal = rs.getDouble("carbs");
                dailyCholesterolGoal = rs.getDouble("cholesterol");
                dailyFatsGoal = rs.getDouble("fats");
                dailySodiumGoal = rs.getDouble("sodium");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("calorieGoal", dailyCalorieGoal);
        model.addAttribute("proteinGoal", dailyProteinGoal);
        model.addAttribute("carbsGoal", dailyCarbsGoal);
        model.addAttribute("cholesterolGoal", dailyCholesterolGoal);
        model.addAttribute("fatsGoal", dailyFatsGoal);
        model.addAttribute("sodiumGoal", dailySodiumGoal);

        double weeklyCalorieGoal = 0.0;
        double weeklyProteinGoal = 0.0;
        double weeklyCarbsGoal = 0.0;
        double weeklyCholesterolGoal = 0.0;
        double weeklyFatsGoal = 0.0;
        double weeklySodiumGoal = 0.0;

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "SELECT calories, proteins, fats, sodium, cholesterol, carbs FROM weekly_goals ORDER BY id DESC LIMIT 1";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                weeklyCalorieGoal = rs.getDouble("calories");
                weeklyProteinGoal = rs.getDouble("proteins");
                weeklyFatsGoal = rs.getDouble("fats");
                weeklyCholesterolGoal = rs.getDouble("cholesterol");
                weeklySodiumGoal = rs.getDouble("sodium");
                weeklyCarbsGoal = rs.getDouble("carbs");


                System.out.printf("Weekly Goal: %f, %f, %f\n", weeklyCalorieGoal, weeklyProteinGoal, weeklyFatsGoal);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("weeklyCaloriesGoal", weeklyCalorieGoal);
        model.addAttribute("weeklyProteinGoal", weeklyProteinGoal);
        model.addAttribute("weeklyCarbsGoal", weeklyCarbsGoal);
        model.addAttribute("weeklyFatsGoal", weeklyFatsGoal);
        model.addAttribute("weeklySodiumGoal", weeklySodiumGoal);
        model.addAttribute("weeklyCholesterolGoal", weeklyCholesterolGoal);

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
            @RequestParam double sugar,
            @RequestParam double fats,
            @RequestParam double saturated_fat,
            @RequestParam double trans_fat,
            @RequestParam double sodium,
            @RequestParam double cholesterol,
            RedirectAttributes ra) {

        service.addFood(name, calories, fats, saturated_fat, trans_fat, cholesterol, sodium, carbs, fiber,
                sugar, protein);

        ra.addFlashAttribute("msg", "Food added successfully");

        return "redirect:/dashboard";
    }

    @GetMapping("/goals")
    public String goals(Model model, HttpSession session) {

        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }

        double dailyCalorieGoal = 0.0;
        double dailyProteinGoal = 0.0;
        double dailyCarbsGoal = 0.0;
        double dailyCholesterolGoal = 0.0;
        double dailyFatsGoal = 0.0;
        double dailySodiumGoal = 0.0;

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "SELECT calories, fats, cholesterol, sodium, carbs, proteins FROM daily_goals ORDER BY id DESC LIMIT 1";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dailyCalorieGoal = rs.getDouble("calories");
                dailyProteinGoal = rs.getDouble("proteins");
                dailyCarbsGoal = rs.getDouble("carbs");
                dailyCholesterolGoal = rs.getDouble("cholesterol");
                dailyFatsGoal = rs.getDouble("fats");
                dailySodiumGoal = rs.getDouble("sodium");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("calorieGoal", dailyCalorieGoal);
        model.addAttribute("proteinGoal", dailyProteinGoal);
        model.addAttribute("carbsGoal", dailyCarbsGoal);
        model.addAttribute("cholesterolGoal", dailyCholesterolGoal);
        model.addAttribute("fatsGoal", dailyFatsGoal);
        model.addAttribute("sodiumGoal", dailySodiumGoal);

        double weeklyCalorieGoal = 0.0;
        double weeklyProteinGoal = 0.0;
        double weeklyCarbsGoal = 0.0;
        double weeklyCholesterolGoal = 0.0;
        double weeklyFatsGoal = 0.0;
        double weeklySodiumGoal = 0.0;

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "SELECT calories, proteins, fats, sodium, cholesterol, carbs FROM weekly_goals ORDER BY id DESC LIMIT 1";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                weeklyCalorieGoal = rs.getDouble("calories");
                weeklyProteinGoal = rs.getDouble("proteins");
                weeklyFatsGoal = rs.getDouble("fats");
                weeklyCholesterolGoal = rs.getDouble("cholesterol");
                weeklySodiumGoal = rs.getDouble("sodium");
                weeklyCarbsGoal = rs.getDouble("carbs");


                System.out.printf("Weekly Goal: %f, %f, %f\n", weeklyCalorieGoal, weeklyProteinGoal, weeklyFatsGoal);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("weeklyCalorieGoal", weeklyCalorieGoal);
        model.addAttribute("weeklyProteinGoal", weeklyProteinGoal);
        model.addAttribute("weeklyCarbsGoal", weeklyCarbsGoal);
        model.addAttribute("weeklyFatsGoal", weeklyFatsGoal);
        model.addAttribute("weeklySodiumGoal", weeklySodiumGoal);
        model.addAttribute("weeklyCholesterolGoal", weeklyCholesterolGoal);

        return "goals";
    }

    @PostMapping("/goals")
    public String updateGoals(@RequestParam String goalType,
            @RequestParam double calorieGoal,
            @RequestParam double proteinGoal,
            @RequestParam double fatsGoal,
            @RequestParam double cholesterolGoal,
            @RequestParam double carbsGoal,
            @RequestParam double sodiumGoal,
            RedirectAttributes ra) {

        if (goalType.equals("daily")) {
            service.addDailyGoal(calorieGoal, fatsGoal, cholesterolGoal, sodiumGoal, carbsGoal, proteinGoal);
        } else if (goalType.equals("weekly")) {
            service.addWeeklyGoal(calorieGoal, fatsGoal, cholesterolGoal, sodiumGoal, carbsGoal, proteinGoal);
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

            String sql = "SELECT name, calories, fats, saturated_fat, trans_fat, cholesterol, sodium, carbs, fiber, sugar, protein " +
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
                        rs.getDouble("sugar"),
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
                    f.cholesterol * l.servings AS cholesterol,
                    f.sodium * l.servings AS sodium,
                    f.carbs * l.servings AS carbs,
                    f.fiber * l.servings AS fiber,
                    f.sugar * l.servings AS sugar,
                    f.protein * l.servings AS protein
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
                        rs.getDouble("sugar"),
                        rs.getDouble("protein")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logs;
    }
}
