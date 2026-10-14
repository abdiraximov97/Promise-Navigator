
package repository;

import database.DatabaseManager;
import model.Promise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.PromiseCategory;
import model.PromiseStatus;
import model.Priority;

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
            System.out.println("PostgreSQL saqlash xatosi: " + e.getMessage());
            e.printStackTrace();
            return false;
//            System.out.println("SQLite saqlash xatosi:");
//            e.printStackTrace();
//            return false;
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

    public List<Promise> findAll() {
        String sql = "SELECT id, title, description, deadline, status, category, priority "
                + "FROM promises ORDER BY id ASC";
        List<Promise> promises = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                promises.add(new Promise(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        LocalDateTime.parse(resultSet.getString("deadline")),
                        PromiseStatus.valueOf(resultSet.getString("status")),
                        PromiseCategory.valueOf(resultSet.getString("category")),
                        Priority.valueOf(resultSet.getString("priority"))
                ));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("PostgreSQL'dan vazifalarni o'qib bo'lmadi: "
                    + e.getMessage(), e);
        }
        return promises;
    }

    public boolean update(Promise promise) {
        String sql = "UPDATE promises SET title=?, description=?, deadline=?, status=?, "
                + "category=?, priority=? WHERE id=?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, promise.getTitle());
            statement.setString(2, promise.getDescription());
            statement.setString(3, promise.getDeadline().toString());
            statement.setString(4, promise.getStatus().name());
            statement.setString(5, promise.getCategory().name());
            statement.setString(6, promise.getPriority().name());
            statement.setInt(7, promise.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("PostgreSQL yangilash xatosi: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM promises WHERE id=?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("PostgreSQL o'chirish xatosi: " + e.getMessage());
            return false;
        }
    }


    // Bazadagi barcha vadalarni konsolga chiqarish
    public void printAll() {
        String sql = "SELECT * FROM promises ORDER BY id ASC";
//        String sql = "SELECT id, title\n" +
//                "FROM promises\n" +
//                "ORDER BY id;";

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
