import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DailyLog{

    //Calculates the total sum of the Calories column from the foods table
    public int sumTotalCalories() {

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "SELECT SUM(calories) AS total from foods";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

            return 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //prints the total calories
    public int getCalories() {

        int total = 0;

        total = sumTotalCalories();

        return total;

    }

    public int sumTotalProteins() {

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "SELECT SUM(protein) AS total from foods";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

            return 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getProteins() {

        int total = 0;

        total = sumTotalProteins();

        return total;

    }

    public int sumTotalSugars() {

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "SELECT SUM(sugar) AS total from foods";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

            return 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getSugars() {

        int total = 0;

        total = sumTotalSugars();

        return total;

    }

    //Prints all the Totals of each column
    public void printLog() {

        System.out.println("Daily Totals:");
        System.out.println("Calories: " + getCalories());
        System.out.println("Proteins: " + getProteins());
        System.out.println("Sugars: " + getSugars());

    }

}