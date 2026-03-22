import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DailyLog{

    //Calculates the total sum of the Calories column from the foods table
    public double sumTotalCalories() {

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "SELECT SUM(calories) AS total from foods";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("total");
            }

            return 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double sumTotalProteins() {

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "SELECT SUM(protein) AS total from foods";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("total");
            }

            return 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double sumTotalSugars() {

        try (Connection conn = DatabaseManager.connect()) {

            String sql = "SELECT SUM(sugar) AS total from foods";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("total");
            }

            return 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //prints the total calories
    public double getCalories() {

        double total = 0;

        total = sumTotalCalories();

        return total;

    }

    public double getProteins() {

        double total = 0;

        total = sumTotalProteins();

        return total;

    }

    public double getSugars() {

        double total = 0;

        total = sumTotalSugars();

        return total;

    }

    public double compareCalories() {
        double total = 0;

        try (Connection conn = DatabaseManager.connect()) {
            String sql = "SELECT calories AS goal from daily_goals";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                total = resultSet.getDouble("goal") - getCalories();
                return total;
            }

            return 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public double compareProteins() {
        double total = 0;

        try (Connection conn = DatabaseManager.connect()) {
            String sql = "SELECT proteins AS goal from daily_goals";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                total = resultSet.getDouble("goal") - getProteins();
                return total;
            }

            return 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public double compareSugars() {
        double total = 0;

        try (Connection conn = DatabaseManager.connect()) {
            String sql = "SELECT sugars AS goal from daily_goals";

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                total = resultSet.getDouble("goal") - getSugars();
                return total;
            }

            return 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //Prints all the Totals of each column
    public void printLog() {

        System.out.println("");
        System.out.println("Daily Totals:");
        System.out.println("Until Calories Limit: " + compareCalories());
        System.out.println("Until Protein Limit: " + compareProteins());
        System.out.println("Until Sugars Limit: " + compareSugars());
        System.out.println("Current Calories: " + getCalories());
        System.out.println("Current Proteins: " + getProteins());
        System.out.println("Current Sugars: " + getSugars());

    }

}