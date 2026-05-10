package com.example.demo;

import org.jspecify.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

//Retrieves information of all food in current log database (All servings of food logged for the day)
public class DailyLog {
    String today = LocalDate.now().toString();

    public double getCalories() {

        String sql = """
                SELECT SUM(f.calories * l.servings)
                FROM food_logs l
                JOIN foods f ON l.food_id = f.id
                WHERE l.date = ?
                """;

        return getValue(sql);
    }

    public double getProteins() {

        String sql = """
                SELECT SUM(f.protein * l.servings)
                FROM food_logs l
                JOIN foods f ON l.food_id = f.id
                WHERE l.date = ?
                """;

        return getValue(sql);
    }

    public double getCarbs() {

        String sql = """
                SELECT SUM(f.carbs * l.servings)
                FROM food_logs l
                JOIN foods f ON l.food_id = f.id
                WHERE l.date = ?
                """;

        return getValue(sql);
    }

    public double getFiber() {

        String sql = """
                SELECT SUM(f.fiber * l.servings)
                FROM food_logs l
                JOIN foods f ON l.food_id = f.id
                WHERE l.date = ?
                """;

        return getValue(sql);
    }

    public double getSaturated_fat() {

        String sql = """
                SELECT SUM(f.saturated_fat * l.servings)
                FROM food_logs l
                JOIN foods f ON l.food_id = f.id
                WHERE l.date = ?
                """;

        return getValue(sql);
    }

    public double getTrans_fat() {

        String sql = """
                SELECT SUM(f.trans_fat * l.servings)
                FROM food_logs l
                JOIN foods f ON l.food_id = f.id
                WHERE l.date = ?
                """;

        return getValue(sql);
    }

    public double getSugars() {

        String sql = """
                SELECT SUM(f.sugar * l.servings)
                FROM food_logs l
                JOIN foods f ON l.food_id = f.id
                WHERE l.date = ?
                """;

        return getValue(sql);
    }


    public double getFats() {

        String sql = """
                SELECT SUM(f.fats * l.servings)
                FROM food_logs l
                JOIN foods f ON l.food_id = f.id
                WHERE l.date = ?
                """;

        return getValue(sql);
    }

    public double getSodium() {

        String sql = """
                SELECT SUM(f.sodium * l.servings)
                FROM food_logs l
                JOIN foods f ON l.food_id = f.id
                WHERE l.date = ?
                """;

        return getValue(sql);
    }

    public double getCholesterol() {

        String sql = """
                SELECT SUM(f.cholesterol * l.servings)
                FROM food_logs l
                JOIN foods f ON l.food_id = f.id
                WHERE l.date = ?
                """;

        return getValue(sql);
    }

    private double getValue(String sql) {

        try (Connection conn = DatabaseManager.connect()) {
             PreparedStatement stmt = conn.prepareStatement(sql);
             stmt.setString(1, today);
             ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double value = rs.getDouble(1);
                if (rs.wasNull()) {
                    return 0;
                }
                return value;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}