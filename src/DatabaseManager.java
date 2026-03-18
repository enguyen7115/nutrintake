import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:nutrition.db";

    public static Connection connect() throws Exception {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS foods (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT,
                    calories INTEGER,
                    protein INTEGER,
                    sugar INTEGER
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS food_logs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    food_id INTEGER,
                    servings INTEGER,
                    date TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS daily_goals (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    calories INTEGER,
                    protein INTEGER,
                    sugar INTEGER
                );
            """);

            stmt.execute("""
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    salt TEXT NOT NULL,
    passwordHash TEXT NOT NULL
)
""");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
