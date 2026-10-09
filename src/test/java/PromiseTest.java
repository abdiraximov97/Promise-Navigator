package test.java;
import model.Promise;
import model.PromiseCategory;
import model.PromiseStatus;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PromiseTest {

    @Test
    void newPromiseShouldHavePendingStatus() {
        // Yangi Promise yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Boshlang'ich status PENDING bo'lishi kerak.
        assertEquals(PromiseStatus.PENDING, promise.getStatus());
    }

    @Test
    void startShouldChangeStatusToInProgress() {
        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani boshlaymiz.
        boolean result = promise.start();

        // Vazifa muvaffaqiyatli boshlanganini tekshiramiz.
        assertTrue(result);

        // Holat IN_PROGRESS bo'lganini tekshiramiz.
        assertEquals(PromiseStatus.IN_PROGRESS, promise.getStatus());
    }


    @Test
    void shouldNotStartPromiseTwice() {
        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Birinchi urinish muvaffaqiyatli bo'lishi kerak.
        assertTrue(promise.start());

        // Ikkinchi urinish muvaffaqiyatsiz bo'lishi kerak.
        assertFalse(promise.start());

        // Vazifa IN_PROGRESS holatida qolishi kerak.
        assertEquals(PromiseStatus.IN_PROGRESS, promise.getStatus());
    }


    @Test
    void completeShouldChangeStatusToCompleted() {
        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Avval vazifani boshlaymiz.
        promise.start();

        // Vazifani bajarilgan deb belgilaymiz.
        boolean result = promise.complete();

        // Bajarish muvaffaqiyatli bo'lganini tekshiramiz.
        assertTrue(result);

        // Vazifa COMPLETED holatiga o'tganini tekshiramiz.
        assertEquals(PromiseStatus.COMPLETED, promise.getStatus());

        // Vazifa bajarilgan deb tan olinishini tekshiramiz.
        assertTrue(promise.isCompleted());
    }


    @Test
    void shouldNotCompletePromiseBeforeStarting() {
        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifa hali boshlanmagan, shuning uchun bajarib bo'lmaydi.
        assertFalse(promise.complete());

        // Holat o'zgarmaganini tekshiramiz.
        assertEquals(PromiseStatus.PENDING, promise.getStatus());
    }


    @Test
    void cancelShouldChangeStatusToCancelled() {
        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani bekor qilamiz.
        boolean result = promise.cancel();

        // Bekor qilish muvaffaqiyatli bo'lganini tekshiramiz.
        assertTrue(result);

        // Holat CANCELLED bo'lganini tekshiramiz.
        assertEquals(PromiseStatus.CANCELLED, promise.getStatus());
    }


    @Test
    void shouldNotCancelPromiseTwice() {
        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Birinchi bekor qilish muvaffaqiyatli bo'lishi kerak.
        assertTrue(promise.cancel());

        // Ikkinchi bekor qilish muvaffaqiyatsiz bo'lishi kerak.
        assertFalse(promise.cancel());

        // Holat CANCELLED bo'lib qolishi kerak.
        assertEquals(PromiseStatus.CANCELLED, promise.getStatus());
    }


    @Test
    void updateDeadlineShouldChangeDeadline() {
        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Yangi muddat belgilaymiz.
        LocalDateTime newDeadline = LocalDateTime.now().plusDays(5);

        // Vazifaning muddatini yangilaymiz.
        promise.updateDeadline(newDeadline);

        // Yangi muddat saqlanganini tekshiramiz.
        assertEquals(newDeadline, promise.getDeadline());
    }


    @Test
    void updateDeadlineShouldRejectPastDeadline() {
        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // O'tgan muddat berilganda xatolik chiqishini tekshiramiz.
        assertThrows(
                IllegalArgumentException.class,
                () -> promise.updateDeadline(LocalDateTime.now().minusDays(1))
        );
    }


    @Test
    void updateTitleShouldChangeTitle() {
        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifa nomini yangilaymiz.
        promise.updateTitle("Java loyihasini tugatish");

        // Yangi nom saqlanganini tekshiramiz.
        assertEquals("Java loyihasini tugatish", promise.getTitle());
    }


    @Test
    void updateTitleShouldRejectBlankTitle() {
        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Bo'sh nom berilganda xatolik chiqishi kerak.
        assertThrows(
                IllegalArgumentException.class,
                () -> promise.updateTitle("   ")
        );

        // Eski nom o'zgarmaganini tekshiramiz.
        assertEquals("java o'rganish", promise.getTitle());
    }


//    @Test
//    void updateDescriptionShouldRejectBlankDescription() {
//        // Yangi vazifa yaratamiz.
//        Promise promise = new Promise(
//                "Java o'rganish",
//                "OOP mavzusini o'rganish",
//                LocalDateTime.now().plusDays(2),
//                PromiseCategory.STUDY
//        );
//
//        // Bo'sh izoh berilganda xatolik chiqishi kerak.
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> promise.updateDescription("   ")
//        );
//
//        // Eski izoh saqlanib qolganini tekshiramiz.
//        assertEquals(
//                "oop mavzusini o'rganish",
//                promise.getDescription()
//        );
//    }


//    @Test
//    void updateCategoryShouldChangeCategory() {
//        // STUDY kategoriyasida vazifa yaratamiz.
//        Promise promise = new Promise(
//                "Java o'rganish",
//                "OOP mavzusini o'rganish",
//                LocalDateTime.now().plusDays(2),
//                PromiseCategory.STUDY
//        );
//
//        // Kategoriyani WORK ga o'zgartiramiz.
//        promise.updateCategory(PromiseCategory.WORK);
//
//        // Yangi kategoriya saqlanganini tekshiramiz.
//        assertEquals(PromiseCategory.WORK, promise.getCategory());
//    }


    @Test
    void updateDescriptionShouldRejectBlankDescription() {
        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );
        // Bo'sh izoh berilganda xatolik chiqishi kerak.
        assertThrows(
                IllegalArgumentException.class,
                () -> promise.updateDescription("   ")
        );
        // Eski izoh saqlanib qolganini tekshiramiz.
        assertEquals(
                "oop mavzusini o'rganish",
                promise.getDescription()
        );
    }
    @Test
    void updateCategoryShouldChangeCategory() {
        // STUDY kategoriyasida vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );
        // Kategoriyani WORK ga o'zgartiramiz.
        promise.updateCategory(PromiseCategory.WORK);
        // Yangi kategoriya saqlanganini tekshiramiz.
        assertEquals(PromiseCategory.WORK, promise.getCategory());
    }


    @Test
    void updateCategoryShouldRejectNull() {
        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // null kategoriya berilganda xatolik chiqishi kerak.
        assertThrows(
                IllegalArgumentException.class,
                () -> promise.updateCategory(null)
        );

        // Eski kategoriya o'zgarmaganini tekshiramiz.
        assertEquals(PromiseCategory.STUDY, promise.getCategory());
    }

}
