package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Database {

    // SQLite bazaga ulanish
    private static final String URL = "jdbc:sqlite:library.db";

    public static void main(String[] args) {

        // Foydalanuvchidan ma'lumot olish uchun
        Scanner scanner = new Scanner(System.in);

        // Bazaga ulanish va jadval yaratish
        try (Connection connection = DriverManager.getConnection(URL);
             Statement statement = connection.createStatement()) {

            // Agar jadval mavjud bo'lmasa, yangisini yaratadi
            String createTable = """
                    CREATE TABLE IF NOT EXISTS books (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        title TEXT NOT NULL,
                        author TEXT NOT NULL
                    )
                    """;

            statement.executeUpdate(createTable);

            System.out.println("Baza va jadval tayyor!");

            // Kitob ma'lumotlarini kiritish
            System.out.print("Kitob nomini kiriting: ");
            String title = scanner.nextLine();

            System.out.print("Muallif ismini kiriting: ");
            String author = scanner.nextLine();

            // Ma'lumotni bazaga yozish
            String insertSql =
                    "INSERT INTO books (title, author) VALUES (?, ?)";

            try (PreparedStatement preparedStatement =
                         connection.prepareStatement(insertSql)) {

                preparedStatement.setString(1, title);
                preparedStatement.setString(2, author);

                int rows = preparedStatement.executeUpdate();

                System.out.println(rows + " ta kitob saqlandi!");
            }

            // Bazadan barcha kitoblarni o'qish
            String selectSql = "SELECT * FROM books";

            try (ResultSet resultSet =
                         statement.executeQuery(selectSql)) {

                System.out.println("\n===== KITOBLAR RO'YXATI =====");

                while (resultSet.next()) {

                    int id = resultSet.getInt("id");
                    String bookTitle = resultSet.getString("title");
                    String bookAuthor = resultSet.getString("author");

                    System.out.println(
                            id + ". " + bookTitle
                                    + " — " + bookAuthor
                    );
                }
            }

            deleteBookById(1);

        } catch (SQLException e) {
            System.out.println("Xatolik: " + e.getMessage());
        }

        scanner.close();
    }

    public static void deleteBookById(int id) {
        String deleteSql = "DELETE FROM books WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(deleteSql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows + " ta kitob o'chirildi!");
        } catch (SQLException e) {
            System.out.println("Xatolik: " + e.getMessage());
        }
    }
}
