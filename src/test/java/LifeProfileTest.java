package test.java;
import model.LifeProfile;
import model.Promise;
import model.PromiseCategory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LifeProfileTest {

    @Test
    void addPromiseShouldStorePromise() {
        // Yangi profil yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");

        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        profile.addPromise(promise);

        // Vazifa profil ichida saqlanganini tekshiramiz.
        assertTrue(profile.getPromises().contains(promise));

        // Profil ichida bitta vazifa borligini tekshiramiz.
        assertEquals(1, profile.getPromises().size());
    }


    @Test
    void findPromiseByIdShouldReturnCorrectPromise() {
        // Yangi profil yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");

        // Birinchi vazifani yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        profile.addPromise(promise);

        // Vazifani uning ID raqami orqali qidiramiz.
        Promise found = profile.findPromiseById(promise.getId());

        // Topilgan obyekt aynan o'sha vazifa ekanini tekshiramiz.
        assertSame(promise, found);

        // Topilgan vazifaning ID raqami to'g'ri ekanini tekshiramiz.
        assertEquals(promise.getId(), found.getId());
    }


    @Test
    void findPromiseByIdShouldReturnNullWhenNotFound() {
        // Yangi profil yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");

        // Hali profilga hech qanday vazifa qo'shmaymiz.
        // Shuning uchun qidiruv natijasi null bo'lishi kerak.
        Promise found = profile.findPromiseById(999999);

        // Vazifa topilmaganini tekshiramiz.
        assertNull(found);
    }


    @Test
    void removePromiseShouldRemovePromiseById() {
        // Yangi profil yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");

        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        profile.addPromise(promise);

        // Vazifani ID orqali o'chiramiz.
        Promise removed = profile.removePromise(promise.getId());

        // O'chirilgan obyekt to'g'ri ekanini tekshiramiz.
        assertSame(promise, removed);

        // Profil bo'sh qolganini tekshiramiz.
        assertTrue(profile.getPromises().isEmpty());
    }


    @Test
    void removePromiseShouldReturnNullWhenIdNotFound() {
        // Yangi profil yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");

        // Mavjud bo'lmagan ID orqali o'chirishga harakat qilamiz.
        Promise removed = profile.removePromise(999999);

        // Hech qanday vazifa topilmagani uchun null qaytishi kerak.
        assertNull(removed);

        // Profil ro'yxati o'zgarmaganini tekshiramiz.
        assertTrue(profile.getPromises().isEmpty());
    }


    @Test
    void updatePromiseShouldChangeTitle() {
        // Yangi profil yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");

        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        profile.addPromise(promise);

        // Vazifa nomini yangilaymiz.
        profile.updatePromise(
                promise.getId(),
                "Java loyihasini tugatish",
                null,
                null,
                null
        );

        // Yangi nom saqlanganini tekshiramiz.
        assertEquals(
                "Java loyihasini tugatish",
                promise.getTitle()
        );
    }


    @Test
    void updatePromiseShouldChangeDescription() {
        // Yangi profil yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");

        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        profile.addPromise(promise);

        // Faqat izohni yangilaymiz.
        profile.updatePromise(
                promise.getId(),
                null,
                "Inheritance mavzusini o'rganish",
                null,
                null
        );

        // Yangi izoh saqlanganini tekshiramiz.
        assertEquals(
                "Inheritance mavzusini o'rganish",
                promise.getDescription()
        );

        // Vazifa nomi o'zgarmaganini ham tekshiramiz.
        assertEquals("java o'rganish", promise.getTitle());
    }


    @Test
    void updatePromiseShouldChangeDeadline() {
        // Yangi profil yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");

        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        profile.addPromise(promise);

        // Yangi muddatni belgilaymiz.
        LocalDateTime newDeadline = LocalDateTime.now().plusDays(5);

        // Faqat muddatni yangilaymiz.
        profile.updatePromise(
                promise.getId(),
                null,
                null,
                newDeadline,
                null
        );

        // Yangi muddat saqlanganini tekshiramiz.
        assertEquals(newDeadline, promise.getDeadline());

        // Vazifa nomi o'zgarmaganini tekshiramiz.
        assertEquals("java o'rganish", promise.getTitle());
    }


    @Test
    void updatePromiseShouldChangeCategory() {
        // Yangi profil yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");

        // STUDY kategoriyasida vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        profile.addPromise(promise);

        // Faqat kategoriyani WORK ga o'zgartiramiz.
        profile.updatePromise(
                promise.getId(),
                null,
                null,
                null,
                PromiseCategory.WORK
        );

        // Yangi kategoriya saqlanganini tekshiramiz.
        assertEquals(PromiseCategory.WORK, promise.getCategory());

        // Nom o'zgarmaganini tekshiramiz.
        assertEquals("java o'rganish", promise.getTitle());
    }


    @Test
    void updatePromiseShouldNotChangeOtherPromisesWhenIdNotFound() {
        // Yangi profil yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");

        // Bitta vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        profile.addPromise(promise);

        // Mavjud bo'lmagan ID orqali yangilashga harakat qilamiz.
        profile.updatePromise(
                999999,
                "Yangi nom",
                "Yangi izoh",
                LocalDateTime.now().plusDays(5),
                PromiseCategory.WORK
        );

        // Asl vazifa o'zgarmaganini tekshiramiz.
        assertEquals("java o'rganish", promise.getTitle());
        assertEquals("oop mavzusini o'rganish", promise.getDescription());
        assertEquals(PromiseCategory.STUDY, promise.getCategory());

        // Profil ichida vazifa saqlanib qolganini tekshiramiz.
        assertTrue(profile.getPromises().contains(promise));
    }
}