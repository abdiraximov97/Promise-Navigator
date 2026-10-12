
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    // SQLite bazasi saqlanadigan fayl
    private static final String URL = "jdbc:sqlite:life_navigator.db";

    // Baza bilan ulanishni qaytaradi
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Jadval mavjud bo'lmasa, uni yaratadi
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

        // Resurslar avtomatik yopiladi
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            // SQL jadval yaratish buyrug'ini bajaradi
            statement.execute(sql);

            System.out.println("SQLite jadvali tayyor.");

        } catch (SQLException e) {
            System.out.println("Jadval yaratishda xatolik: " + e.getMessage());
        }
    }
}
