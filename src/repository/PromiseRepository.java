
package repository;

import database.DatabaseManager;
import model.Promise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PromiseRepository {

    // Bitta vadani bazaga saqlash
    public boolean save(
            int id,
            String title,
            String description,
            String deadline,
            String status,
            String category,
            String priority
    ) {
        String sql = """
                INSERT INTO promises
                (id, title, description, deadline, status, category, priority)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setString(4, deadline);
            statement.setString(5, status);
            statement.setString(6, category);
            statement.setString(7, priority);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("SQLite saqlash xatosi:");
            e.printStackTrace();
            return false;
        }
    }


    public boolean save(Promise promise) {
        return save(
                promise.getId(),
                promise.getTitle(),
                promise.getDescription(),
                promise.getDeadline().toString(),
                promise.getStatus().name(),
                promise.getCategory().name(),
                promise.getPriority().name()
        );
    }


    // Bazadagi barcha vadalarni konsolga chiqarish
    public void printAll() {
        String sql = "SELECT * FROM promises ORDER BY id ASC";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                System.out.println(
                        "ID: " + resultSet.getInt("id")
                                + " | Sarlavha: " + resultSet.getString("title")
                                + " | Tavsif: " + resultSet.getString("description")
                                + " | Muddat: " + resultSet.getString("deadline")
                                + " | Holat: " + resultSet.getString("status")
                                + " | Kategoriya: " + resultSet.getString("category")
                                + " | Muhimlik: " + resultSet.getString("priority")
                );
            }

        } catch (SQLException e) {
            System.out.println("Vadalarni o‘qishda xatolik: " + e.getMessage());
        }
    }
}
