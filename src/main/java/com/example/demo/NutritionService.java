package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class NutritionService {
    //Allows user to create a food item with a name and certain nutritional information and
    //add to food database
    public void addFood(String name, double calories, double fats, double saturated_fat, double trans_fat,
                        double cholesterol, double sodium, double carbs, double fiber, double sugars, double protein) {

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "INSERT INTO foods(name, calories, fats, saturated_fat, trans_fat, cholesterol, sodium, " +
                    "carbs, fiber, sugars, protein) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setDouble(2, calories);
            stmt.setDouble(3, fats);
            stmt.setDouble(4, saturated_fat);
            stmt.setDouble(5, trans_fat);
            stmt.setDouble(6, cholesterol);
            stmt.setDouble(7, sodium);
            stmt.setDouble(8, carbs);
            stmt.setDouble(9, fiber);
            stmt.setDouble(10, sugars);
            stmt.setDouble(11, protein);

            stmt.executeUpdate();

            String logsql = "INSERT INTO food_logs(date) VALUES (?)";
            PreparedStatement logstmt = conn.prepareStatement(logsql);
            logstmt.setString(1, LocalDate.now().toString());
            logstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Allows DatabaseManager to log food entries to database
    public void logFood(String foodName, double servings){
        //Connects to the database
        try (Connection conn = DatabaseManager.connect()) {
            String getFood = "SELECT * FROM foods WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(getFood);
            stmt.setString(1, foodName);

            ResultSet rs = stmt.executeQuery();

            //Inserts food choices into the food_log database
            if (rs.next()) {
                int foodId = rs.getInt("id");

                String sql = "INSERT INTO food_logs(food_id, servings, date) VALUES (?, ?, ?)";

                PreparedStatement insert = conn.prepareStatement(sql);

                insert.setInt(1, foodId);
                insert.setDouble(2, servings);
                insert.setString(3, LocalDate.now().toString());

                insert.executeUpdate();
            }
        //If any part of the program fails, create an exception
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //Allows user to submit goal for each day
    public void addDailyGoal(double caloriesGoal, double fatsGoal, double cholesterolGoal, double sodiumGoal,
                             double carbsGoal, double proteinGoal) {

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "INSERT INTO daily_goals(calories, fats, cholesterol, sodium, carbs, proteins) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, caloriesGoal);
            stmt.setDouble(2, fatsGoal);
            stmt.setDouble(3, cholesterolGoal);
            stmt.setDouble(4, sodiumGoal);
            stmt.setDouble(5, carbsGoal);
            stmt.setDouble(6, proteinGoal);

            stmt.executeUpdate();
        }

        catch (Exception e) {
            System.out.println("Exception in addDailyGoal");
        }
    }

    //Adds current daily log to weekly log database
    //TODO: Add daily log to weekly log database through frontend call
    public void addWeekly(double totalCalories, double totalProtein, double totalSugar) {

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "INSERT INTO weekly_logs(logDate, calories, protein, sugar) VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, LocalDate.now().toString());
            stmt.setDouble(2, totalCalories);
            stmt.setDouble(3, totalProtein);
            stmt.setDouble(4, totalSugar);

            stmt.executeUpdate();

        }

        catch (Exception e) {
            System.out.println("Exception in addWeekly");
        }

    }

    //TODO: Delete daily log through frontend call to database
    public void deleteDaily() {

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "DELETE FROM foods";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //TODO: Delete entries from weekly log through frontend call
    public void deleteWeekly() {

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "DELETE FROM weekly_logs";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //TODO: Create call to weekly log database and save information
    public void saveToWeekly(){
        DailyLog dailyLog = new DailyLog();

        double calories = dailyLog.getCalories();
        double protein = dailyLog.getProteins();
        double sugar = dailyLog.getSugars();

        addWeekly(calories, protein, sugar);

    }


}
