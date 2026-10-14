
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
import java.sql.Timestamp;

public class PromiseRepository {

    // Bitta vadani bazaga saqlash


    public int save(
            String title,
            String description,
            String deadline,
            String status,
            String category,
            String priority
    ) {
        String sql = """
            INSERT INTO promises
            (title, description, deadline, status, category, priority)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING id
            """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.setString(2, description);
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.parse(deadline)));
            statement.setString(4, status);
            statement.setString(5, category);
            statement.setString(6, priority);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }

            return -1;

        } catch (SQLException | java.time.format.DateTimeParseException e) {
            System.out.println("PostgreSQL saqlash xatosi: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public boolean save(Promise promise) {
        int databaseId = save(
                promise.getTitle(),
                promise.getDescription(),
                promise.getDeadline().toString(),
                promise.getStatus().name(),
                promise.getCategory().name(),
                promise.getPriority().name()
        );

        if (databaseId <= 0) {
            return false;
        }

        promise.setId(databaseId);
        return true;
    }

    public List<Promise> findAll() {
        String sql = "SELECT id, title, description, deadline, status, "
                + "category, priority, updated_at "
                + "FROM promises ORDER BY id ASC";

        List<Promise> promises = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Promise promise = new Promise(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("deadline").toLocalDateTime(),
                        PromiseStatus.valueOf(resultSet.getString("status")),
                        PromiseCategory.valueOf(resultSet.getString("category")),
                        Priority.valueOf(resultSet.getString("priority"))
                );

                Timestamp updatedTimestamp = resultSet.getTimestamp("updated_at");

                if (updatedTimestamp != null) {
                    promise.setUpdatedAt(updatedTimestamp.toLocalDateTime());
                }

                promises.add(promise);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "PostgreSQL'dan vazifalarni o'qib bo'lmadi: "
                            + e.getMessage(), e
            );
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
            statement.setTimestamp(3, Timestamp.valueOf(promise.getDeadline()));
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
