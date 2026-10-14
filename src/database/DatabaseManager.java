package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    // Sozlamalarni environment variables orqali berish mumkin.
    private static final String URL = setting(
            "DB_URL", "jdbc:postgresql://localhost:5432/promise_navigator"
    );
    private static final String USER = setting("DB_USER", "postgres");
    private static final String PASSWORD = setting("DB_PASSWORD", "postgres");

    private static String setting(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS promises (
                    id INTEGER PRIMARY KEY,
                    title TEXT NOT NULL,
                    description TEXT,
                    deadline TEXT,
                    status TEXT NOT NULL,
                    category TEXT,
                    priority TEXT
                )
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("PostgreSQL jadvali tayyor.");
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "PostgreSQL'ga ulanish yoki jadval yaratishda xatolik: "
                            + e.getMessage(), e
            );
        }
    }
}
