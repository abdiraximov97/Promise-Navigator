
package repository;

import model.Promise;

import java.time.LocalDateTime;
import java.util.List;

public class PromiseRepositoryTest {

    public static void main(String[] args) {

        // Repository obyektini yaratamiz.
        PromiseRepository repository = new PromiseRepository();

        // Yangi test vazifasini yaratamiz.
        Promise promise = new Promise(
                "PostgreSQL test",
                "Bazaga saqlashni tekshirish",
                LocalDateTime.now().plusDays(2),
                model.PromiseCategory.PERSONAL
        );

        // Vazifani PostgreSQL bazasiga saqlaymiz.
        boolean saved = repository.save(promise);

        System.out.println("Saqlash natijasi: " + saved);
        System.out.println("Test vazifasining ID si: " + promise.getId());

        // Bazadagi barcha vazifalarni qayta o‘qiymiz.
        List<Promise> promises = repository.findAll();

        // Natijalarni ekranga chiqaramiz.
        for (Promise item : promises) {
            System.out.println(item);
        }
    }
}
