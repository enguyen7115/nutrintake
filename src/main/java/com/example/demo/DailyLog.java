package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//Retrieves information of all food in current log database (All servings of food logged for the day)
public class DailyLog {

    public double getCalories() {

        String sql = """
                SELECT SUM(f.calories * l.servings)
                FROM food_logs l
                JOIN foods f ON l.food_id = f.id
                """;

        return getValue(sql);
    }
    
    public double getCaloriesForDay(LocalDate date) {
        return getCaloriesForRange(date, date);
    }

    public double getCaloriesForRange(LocalDate start, LocalDate end) {
        String sql = """
                     SELECT SUM(f.calories * l.servings)
                     FROM food_logs l
                     JOIN foods f ON l.food_id = f.id
                     WHERE l.date BETWEEN ? AND ?
                     """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, start.toString());
            stmt.setString(2, end.toString());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getProteins() {

        String sql = """
                SELECT SUM(f.protein * l.servings)
                FROM food_logs l
                JOIN foods f ON l.food_id = f.id
                """;

        return getValue(sql);
    }

    public double getProteinsForDay(LocalDate date) {
        return getProteinsForRange(date, date);
    }

    public double getProteinsForRange(LocalDate start, LocalDate end) {
        String sql = """
        SELECT SUM(f.protein * l.servings)
        FROM food_logs l
        JOIN foods f ON l.food_id = f.id
        WHERE l.date BETWEEN ? AND ?
    """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, start.toString());
            stmt.setString(2, end.toString());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getSugars() {

        String sql = """
                SELECT SUM(f.sugar * l.servings)
                FROM food_logs l
                JOIN foods f ON l.food_id = f.id
                """;

        return getValue(sql);
    }

    public double getSugarsForDay(LocalDate date) {
        return getSugarsForRange(date, date);
    }

    public double getSugarsForRange(LocalDate start, LocalDate end) {
        String sql = """
        SELECT SUM(f.sugar * l.servings)
        FROM food_logs l
        JOIN foods f ON l.food_id = f.id
        WHERE l.date BETWEEN ? AND ?
    """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, start.toString());
            stmt.setString(2, end.toString());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private double getValue(String sql) {

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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
