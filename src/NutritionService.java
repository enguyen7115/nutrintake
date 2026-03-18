import java.sql.Connection;
import java.sql.PreparedStatement;

public class NutritionService {
    public void addFood(String name, int calories, int protein, int sugar) {

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "INSERT INTO foods(name, calories, protein, sugar) VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, calories);
            stmt.setInt(3, protein);
            stmt.setInt(4, sugar);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDailyGoal(int caloriesGoal, int proteinGoal, int sugarGoal) {

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "INSERT INTO daily_goals(calories, protein, sugar) VALUES (?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, caloriesGoal);
            stmt.setInt(2, proteinGoal);
            stmt.setInt(3, sugarGoal);

            stmt.executeUpdate();
        }

        catch (Exception e) {
            System.out.println("Exception in addDailyGoal");
        }
    }
}
