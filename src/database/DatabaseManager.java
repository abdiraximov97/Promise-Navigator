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
    private static final String PASSWORD = "muhammadyusuf";

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
                id SERIAL PRIMARY KEY,
                title VARCHAR(200) NOT NULL,
                description TEXT NOT NULL,
                deadline TIMESTAMP NOT NULL,
                status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
                category VARCHAR(50) NOT NULL
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

    public static void testConnection() {
        try (Connection connection = getConnection()) {
            if (connection.isValid(5)) {
                System.out.println("PostgreSQL bilan ulanish muvaffaqiyatli.");
                System.out.println("Database: " + connection.getCatalog());
            }
        } catch (SQLException e) {
            System.out.println("Ulanishda xatolik: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
