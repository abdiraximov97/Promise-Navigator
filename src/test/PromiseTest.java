package test;
import model.Promise;
import model.PromiseCategory;
import model.PromiseStatus;

import java.time.LocalDateTime;

public class PromiseTest {
    public static void main(String[] args) {
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // 1. Yangi Promise PENDING bo'lishi kerak
        assert promise.getStatus() == PromiseStatus.PENDING;

        // 2. Boshlash muvaffaqiyatli bo'lishi kerak
        assert promise.start();

        // 3. Endi status IN_PROGRESS bo'lishi kerak
        assert promise.getStatus() == PromiseStatus.IN_PROGRESS;

        // 4. Jarayondagi vazifani bajarish mumkin
        assert promise.complete();

        // 5. Status COMPLETED bo'lishi kerak
        assert promise.getStatus() == PromiseStatus.COMPLETED;

        // 6. Bajarilgan vazifani bekor qilib bo'lmaydi
        assert !promise.cancel();

        System.out.println("Barcha testlar muvaffaqiyatli o'tdi!");
    }
}
