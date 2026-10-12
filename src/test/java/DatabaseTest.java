
package test.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseTest {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:life_navigator.db";
        // Qqaysi vazifani statusini o'zgartirishni belgilaymiz
        String sql = " UPDATE promises SET status = ? WHERE id = ? ";

        try (Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sql)){

            // birinchi ? o'rniga yangi status beramiz
//            statement.setString(1, "IN_PROGRESS");

            // o'chirilishi kerak bo'lgan vazifa ID
            statement.setInt(1, 1);

            // ikkinchi ? o'rniga vazifa ID beramiz
            statement.setInt(2, 1);

            int rowsDelete = statement.executeUpdate();

            if (rowsDelete > 0) {
                System.out.println("Vazifa muvaffaqiyatli o'chirildi!");
            } else {
                System.out.println("Bunday ID bilan vazifa topilmadi");
            }

            //so'rovni bajarib o'zgartirgan qatorlar sonini olamiz
//            int rowsUpdated = statement.executeUpdate();
//            if (rowsUpdated > 0) {
//                System.out.println("Vazifa statusi muvaffaqiyatli o'zgartirildi!");
//            } else {
//                System.out.println("Bunday ID bilan vazifa topilmadi");
//            }
        } catch (SQLException e) {
            System.out.println("O'chirishda xatolik yuz berdi:");
            e.printStackTrace();

//            System.out.println("Statusni o'zgartirishda xatolik yuz berdi:");
//            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        // SQLite bazasi fayliga ulanish manzili.
//        String url = "jdbc:sqlite:life_navigator.db";
//
//        // Barcha vazifalarni o'qish uchun SQL so'rovi
//        String sql = "SELECT * FROM promises";
//
//        // Bazaga ulanib resurslarni avto matik yopamiz
//        try (Connection connection = DriverManager.getConnection(url);
//            // SQL so'rovini tayyorlaymiz
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            //SELECT so'rovini bajarib natijani olamiz
//            ResultSet resultSet = preparedStatement.executeQuery()){
//            //Natijalarni har bir qatorini navbat bilan o'qiymiz
//            while (resultSet.next()) {
//                // Har bir ustundagi qiymatlarni olamiz.
//                int id = resultSet.getInt("id");
//                String title = resultSet.getString("title");
//                String description = resultSet.getString("description");
//                String deadline = resultSet.getString("deadline");
//                String status = resultSet.getString("status");
//                String category = resultSet.getString("category");
//                String priority = resultSet.getString("priority");
//
//                // Bitta vazifa ma'lumotlarini konsolga chiqaramiz.
//                System.out.println("========== VAZIFA ==========");
//                System.out.println("ID: " + id);
//                System.out.println("Nomi: " + title);
//                System.out.println("Tavsifi: " + description);
//                System.out.println("Deadline: " + deadline);
//                System.out.println("Status: " + status);
//                System.out.println("Kategoriya: " + category);
//                System.out.println("Ustuvorlik: " + priority);
//            }
//        } catch (SQLException e) {
//            // SQL xatosi yuz bersa, tafsilotlarini chiqaramiz.
//            System.out.println("Vazifani o'qishishda xatolik yuz berdi:");
//            e.printStackTrace();
//        }
//    }

//    public static void main(String[] args) {
//
//        // SQLite bazasi fayliga ulanish manzili.
//        String url = "jdbc:sqlite:life_navigator.db";
//
//        // Jadvalga yangi vazifa qo'shadigan SQL so'rovi.
//        String sql = """
//            INSERT INTO promises
//            (title, description, deadline, status, category, priority)
//            VALUES (?, ?, ?, ?, ?, ?)
//            """;
//
//        // Bazaga ulanamiz va resurslarni avtomatik yopamiz.
//        try (Connection connection = DriverManager.getConnection(url);
//
//             // SQL so'rovini parametrlar bilan tayyorlaymiz.
//             PreparedStatement preparedStatement =
//                     connection.prepareStatement(sql)) {
//
//            // 1-parametr: vazifa nomi.
//            preparedStatement.setString(1, "Java OOP o'rganish");
//
//            // 2-parametr: vazifa tavsifi.
//            preparedStatement.setString(2, "Inheritance mavzusi");
//
//            // 3-parametr: deadline matn ko'rinishida.
//            preparedStatement.setString(3, "2026-10-15T18:00");
//
//            // 4-parametr: vazifa holati.
//            preparedStatement.setString(4, "PENDING");
//
//            // 5-parametr: vazifa kategoriyasi.
//            preparedStatement.setString(5, "STUDY");
//
//            // 6-parametr: vazifa ustuvorligi.
//            preparedStatement.setString(6, "HIGH");
//
//            // SQL so'rovini bajarib, qo'shilgan qatorlar sonini olamiz.
//            int rowsInserted = preparedStatement.executeUpdate();
//
//            // Natijani tekshiramiz.
//            if (rowsInserted > 0) {
//                System.out.println("Vazifa bazaga muvaffaqiyatli qo'shildi!");
//            }
//
//        } catch (SQLException e) {
//            // SQL xatosi yuz bersa, tafsilotlarini chiqaramiz.
//            System.out.println("Vazifani qo'shishda xatolik yuz berdi:");
//            e.printStackTrace();
//        }
//    }

}
