
package test.java;

import model.LifeProfile;
import model.Promise;
import model.PromiseCategory;
import org.junit.jupiter.api.Test;
import service.PromiseService;
import model.PromiseStatus;
import java.time.LocalDateTime;
import  java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

class PromiseServiceTest {

    @Test
    void addPromiseShouldStorePromiseInProfile() {
        // Yangi profil yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");

        // Servisni profil bilan bog'laymiz.
        PromiseService service = new PromiseService(profile);

        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani servis orqali qo'shamiz.
        service.addPromise(promise);

        // Vazifa profil ichida saqlanganini tekshiramiz.
        assertTrue(profile.getPromises().contains(promise));

        // Profil ichida bitta vazifa borligini tekshiramiz.
        assertEquals(1, profile.getPromises().size());
    }


    @Test
    void getAllPromisesShouldReturnAllPromises() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Birinchi vazifani yaratamiz.
        Promise first = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Ikkinchi vazifani yaratamiz.
        Promise second = new Promise(
                "Kitob o'qish",
                "10 sahifa o'qish",
                LocalDateTime.now().plusDays(3),
                PromiseCategory.PERSONAL
        );

        // Ikkala vazifani servis orqali qo'shamiz.
        service.addPromise(first);
        service.addPromise(second);

        // Barcha vazifalarni olamiz.
        var promises = service.getAllPromises();

        // Ikki vazifa qaytganini tekshiramiz.
        assertEquals(2, promises.size());

        // Birinchi vazifa ro'yxatda borligini tekshiramiz.
        assertTrue(promises.contains(first));

        // Ikkinchi vazifa ro'yxatda borligini tekshiramiz.
        assertTrue(promises.contains(second));
    }


    @Test
    void findPromiseByIdShouldReturnCorrectPromise() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani servis orqali qo'shamiz.
        service.addPromise(promise);

        // Vazifani ID orqali qidiramiz.
        Promise found = service.findPromiseById(promise.getId());

        // Topilgan obyekt aynan shu vazifa ekanini tekshiramiz.
        assertSame(promise, found);

        // ID to'g'ri ekanini tekshiramiz.
        assertEquals(promise.getId(), found.getId());
    }


    @Test
    void removePromiseShouldRemovePromiseFromProfile() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Test uchun vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        service.addPromise(promise);

        // Vazifani ID orqali o'chiramiz.
        Promise removed = service.removePromise(promise.getId());

        // O'chirilgan obyekt aynan o'sha vazifa ekanini tekshiramiz.
        assertSame(promise, removed);

        // Profil ichida boshqa vazifa qolmaganini tekshiramiz.
        assertTrue(profile.getPromises().isEmpty());
    }


    @Test
    void removePromiseShouldReturnNullWhenIdDoesNotExist() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Profilga bitta vazifa qo'shamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        service.addPromise(promise);

        // Mavjud bo'lmagan ID bilan vazifani o'chirishga harakat qilamiz.
        Promise removed = service.removePromise(-1);

        // Natija null bo'lishi kerak.
        assertNull(removed);

        // Asl vazifa profilda saqlanib qolganini tekshiramiz.
        assertEquals(1, profile.getPromises().size());
        assertSame(promise, profile.getPromises().get(0));
    }


    @Test
    void startPromiseShouldChangeStatusToInProgress() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        service.addPromise(promise);

        // Vazifa dastlab PENDING holatida ekanini tekshiramiz.
        assertEquals(PromiseStatus.PENDING, promise.getStatus());

        // Vazifani boshlaymiz.
        boolean started = service.startPromise(promise.getId());

        // Vazifa muvaffaqiyatli boshlanganini tekshiramiz.
        assertTrue(started);

        // Vazifa holati IN_PROGRESS bo'lganini tekshiramiz.
        assertEquals(PromiseStatus.IN_PROGRESS, promise.getStatus());
    }


    @Test
    void startPromiseShouldReturnFalseWhenIdDoesNotExist() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Profilga bitta vazifa qo'shamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        service.addPromise(promise);

        // Mavjud bo'lmagan ID bilan vazifani boshlashga urinamiz.
        boolean started = service.startPromise(-1);

        // Vazifa boshlanmaganini tekshiramiz.
        assertFalse(started);

        // Asl vazifa PENDING holatida qolganini tekshiramiz.
        assertEquals(PromiseStatus.PENDING, promise.getStatus());
    }


    @Test
    void startPromiseShouldNotStartTwice() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        service.addPromise(promise);

        // Birinchi urinish muvaffaqiyatli bo'lishi kerak.
        assertTrue(service.startPromise(promise.getId()));

        // Ikkinchi urinish muvaffaqiyatsiz bo'lishi kerak.
        assertFalse(service.startPromise(promise.getId()));

        // Vazifa IN_PROGRESS holatida qolishi kerak.
        assertEquals(PromiseStatus.IN_PROGRESS, promise.getStatus());
    }


    @Test
    void completePromiseShouldChangeStatusToCompleted() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        service.addPromise(promise);

        // Avval vazifani boshlaymiz.
        assertTrue(service.startPromise(promise.getId()));

        // Vazifani bajarilgan deb belgilaymiz.
        boolean completed = service.completePromise(promise.getId());

        // Bajarish muvaffaqiyatli bo'lganini tekshiramiz.
        assertTrue(completed);

        // Vazifa COMPLETED holatiga o'tganini tekshiramiz.
        assertEquals(PromiseStatus.COMPLETED, promise.getStatus());

        // Qo'shimcha tekshiruv: vazifa bajarilganligini tasdiqlaymiz.
        assertTrue(promise.isCompleted());
    }


    @Test
    void completePromiseShouldFailWhenPromiseIsNotStarted() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        service.addPromise(promise);

        // Vazifani boshlamasdan bajarishga harakat qilamiz.
        boolean completed = service.completePromise(promise.getId());

        // Bajarish muvaffaqiyatsiz bo'lishi kerak.
        assertFalse(completed);

        // Vazifa PENDING holatida qolishi kerak.
        assertEquals(PromiseStatus.PENDING, promise.getStatus());

        // Vazifa bajarilgan deb hisoblanmasligi kerak.
        assertFalse(promise.isCompleted());
    }


    @Test
    void cancelPromiseShouldChangeStatusToCancelled() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        service.addPromise(promise);

        // Vazifani bekor qilamiz.
        boolean cancelled = service.cancelPromise(promise.getId());

        // Bekor qilish muvaffaqiyatli bo'lganini tekshiramiz.
        assertTrue(cancelled);

        // Vazifa CANCELLED holatiga o'tganini tekshiramiz.
        assertEquals(PromiseStatus.CANCELLED, promise.getStatus());

        // Bekor qilingan vazifa bajarilgan hisoblanmasligini tekshiramiz.
        assertFalse(promise.isCompleted());
    }


    @Test
    void cancelPromiseShouldFailWhenPromiseIsCompleted() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Yangi vazifa yaratamiz.
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz.
        service.addPromise(promise);

        // Vazifani boshlaymiz.
        assertTrue(service.startPromise(promise.getId()));

        // Vazifani bajarilgan deb belgilaymiz.
        assertTrue(service.completePromise(promise.getId()));

        // Bajarilgan vazifani bekor qilishga urinib ko'ramiz.
        boolean cancelled = service.cancelPromise(promise.getId());

        // Bekor qilish muvaffaqiyatsiz bo'lishi kerak.
        assertFalse(cancelled);

        // Vazifa COMPLETED holatida qolishi kerak.
        assertEquals(PromiseStatus.COMPLETED, promise.getStatus());

        // Vazifa bajarilganligicha qolganini tekshiramiz.
        assertTrue(promise.isCompleted());
    }


    @Test
    void findPromisesByStatusShouldReturnOnlyMatchingPromises() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Birinchi vazifani yaratamiz.
        Promise promise1 = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Ikkinchi vazifani yaratamiz.
        Promise promise2 = new Promise(
                "Kitob o'qish",
                "10 sahifa o'qish",
                LocalDateTime.now().plusDays(3),
                PromiseCategory.PERSONAL
        );

        // Ikkala vazifani profilga qo'shamiz.
        service.addPromise(promise1);
        service.addPromise(promise2);

        // Birinchi vazifani boshlaymiz.
        assertTrue(service.startPromise(promise1.getId()));

        // IN_PROGRESS holatidagi vazifalarni qidiramiz.
        List<Promise> result =
                service.findPromisesByStatus(PromiseStatus.IN_PROGRESS);

        // Faqat bitta vazifa topilishi kerak.
        assertEquals(1, result.size());

        // Topilgan vazifa birinchi vazifa ekanini tekshiramiz.
        assertSame(promise1, result.get(0));

        // Ikkinchi vazifa PENDING holatida qolganini tekshiramiz.
        assertEquals(PromiseStatus.PENDING, promise2.getStatus());
    }


    @Test
    void findPromisesByCategoryShouldReturnOnlyMatchingPromises() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // O'qish kategoriyasidagi vazifa.
        Promise studyPromise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Shaxsiy kategoriyadagi vazifa.
        Promise personalPromise = new Promise(
                "Kitob o'qish",
                "10 sahifa o'qish",
                LocalDateTime.now().plusDays(3),
                PromiseCategory.PERSONAL
        );

        // Ikkala vazifani servisga qo'shamiz.
        service.addPromise(studyPromise);
        service.addPromise(personalPromise);

        // STUDY kategoriyasidagi vazifalarni qidiramiz.
        List<Promise> result =
                service.findPromisesByCategory(PromiseCategory.STUDY);

        // Faqat bitta vazifa topilishi kerak.
        assertEquals(1, result.size());

        // Topilgan vazifa Java o'rganish vazifasi ekanini tekshiramiz.
        assertSame(studyPromise, result.get(0));

        // Topilgan vazifaning kategoriyasini tekshiramiz.
        assertEquals(PromiseCategory.STUDY, result.get(0).getCategory());
    }


    @Test
    void countPromisesByStatusShouldReturnCorrectCount() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Birinchi vazifani yaratamiz.
        Promise promise1 = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Ikkinchi vazifani yaratamiz.
        Promise promise2 = new Promise(
                "Kitob o'qish",
                "10 sahifa o'qish",
                LocalDateTime.now().plusDays(3),
                PromiseCategory.PERSONAL
        );

        // Uchinchi vazifani yaratamiz.
        Promise promise3 = new Promise(
                "Ingliz tili",
                "Yangi so'zlarni o'rganish",
                LocalDateTime.now().plusDays(4),
                PromiseCategory.STUDY
        );

        // Uchala vazifani servisga qo'shamiz.
        service.addPromise(promise1);
        service.addPromise(promise2);
        service.addPromise(promise3);

        // Ikki vazifani boshlaymiz.
        assertTrue(service.startPromise(promise1.getId()));
        assertTrue(service.startPromise(promise2.getId()));

        // IN_PROGRESS holatidagi vazifalar sonini hisoblaymiz.
        int inProgressCount =
                service.countPromisesByStatus(PromiseStatus.IN_PROGRESS);

        // Ikki vazifa jarayonda ekanini tekshiramiz.
        assertEquals(2, inProgressCount);

        // PENDING holatidagi vazifalar sonini hisoblaymiz.
        int pendingCount =
                service.countPromisesByStatus(PromiseStatus.PENDING);

        // Bitta vazifa hali boshlanmaganini tekshiramiz.
        assertEquals(1, pendingCount);
    }


    @Test
    void getUpcomingPromisesShouldReturnPromisesDueWithin24Hours() {
        // Yangi profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // 12 soatdan keyin muddati keladigan vazifa.
        Promise upcomingPromise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusHours(12),
                PromiseCategory.STUDY
        );

        // 2 kundan keyin muddati keladigan vazifa.
        Promise laterPromise = new Promise(
                "Kitob o'qish",
                "10 sahifa o'qish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.PERSONAL
        );

        // Vazifalarni servisga qo'shamiz.
        service.addPromise(upcomingPromise);
        service.addPromise(laterPromise);

        // Yaqin 24 soat ichidagi vazifalarni olamiz.
        List<Promise> result = service.getUpcomingPromises();

        // Faqat bitta vazifa topilishi kerak.
        assertEquals(1, result.size());

        // Topilgan vazifa 12 soatlik muddatga ega ekanini tekshiramiz.
        assertSame(upcomingPromise, result.get(0));
    }

    @Test
    void getOverduePromisesShouldReturnOnlyOverduePromises() {
        // Test uchun belgilangan hozirgi vaqt.
        Instant fixedInstant = Instant.parse("2026-10-09T10:00:00Z");
        Clock fixedClock = Clock.fixed(
                fixedInstant,
                ZoneId.of("UTC")
        );

        // Promise uchun test vaqtini o'rnatamiz.
        Promise.setClock(fixedClock);

        try {
            // Testdagi hozirgi vaqt: 2026-10-09 10:00 UTC.
            LocalDateTime now = LocalDateTime.now(fixedClock);

            LifeProfile profile = new LifeProfile("Shaxboz");
            PromiseService service = new PromiseService(profile);

            // Hozirgi test vaqtidan 2 soat keyin tugaydigan vazifa.
            Promise overduePromise = new Promise(
                    "Vazifani bajarish",
                    "Overdue holatini tekshirish",
                    now.plusHours(2),
                    PromiseCategory.STUDY
            );

            // Kelajakdagi boshqa vazifa.
            Promise futurePromise = new Promise(
                    "Kitob o'qish",
                    "10 sahifa o'qish",
                    now.plusDays(2),
                    PromiseCategory.PERSONAL
            );

            service.addPromise(overduePromise);
            service.addPromise(futurePromise);

            // Vaqtni 3 soat oldinga suramiz.
            Clock laterClock = Clock.fixed(
                    fixedInstant.plusSeconds(3 * 60 * 60),
                    ZoneId.of("UTC")
            );
            Promise.setClock(laterClock);

            // Muddati o'tgan va'dalarni olamiz.
            List<Promise> result = service.getOverduePromises();

            assertEquals(1, result.size());
            assertSame(overduePromise, result.get(0));
            assertEquals(PromiseStatus.OVERDUE, overduePromise.getStatus());

        } finally {
            // Boshqa testlarga ta'sir qilmasligi uchun haqiqiy vaqtni tiklaymiz.
            Promise.setClock(Clock.systemDefaultZone());
        }
    }


    @Test
    void cancelledPromiseShouldNotBecomeOverdue() {
        // Test uchun belgilangan vaqt.
        Instant fixedInstant = Instant.parse("2026-10-09T10:00:00Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.of("UTC"));

        Promise.setClock(fixedClock);

        try {
            // Yangi profil va servis yaratamiz.
            LifeProfile profile = new LifeProfile("Shaxboz");
            PromiseService service = new PromiseService(profile);

            // Kelajakdagi muddat bilan vazifa yaratamiz.
            Promise promise = new Promise(
                    "Kitob o'qish",
                    "10 sahifa o'qish",
                    LocalDateTime.now(fixedClock).plusHours(1),
                    PromiseCategory.PERSONAL
            );

            service.addPromise(promise);

            // Vazifani bekor qilamiz.
            assertTrue(service.cancelPromise(promise.getId()));
            assertEquals(PromiseStatus.CANCELLED, promise.getStatus());

            // Vaqtni muddatdan keyinga o'tkazamiz.
            Clock laterClock = Clock.fixed(
                    fixedInstant.plusSeconds(2 * 60 * 60),
                    ZoneId.of("UTC")
            );
            Promise.setClock(laterClock);

            // Muddati o'tgan vazifalarni tekshiramiz.
            service.chescOverduePromise();

            // Bekor qilingan vazifa CANCELLED bo'lib qolishi kerak.
            assertEquals(PromiseStatus.CANCELLED, promise.getStatus());

            // Muddati o'tganlar ro'yxatiga kirmasligi kerak.
            assertTrue(service.getOverduePromises().isEmpty());

        } finally {
            // Haqiqiy vaqtni tiklaymiz.
            Promise.setClock(Clock.systemDefaultZone());
        }
    }

    @Test
    void completedPromiseShouldNotBecomeOverdue() {
        // Test uchun boshlang'ich vaqtni belgilaymiz.
        Instant fixedInstant = Instant.parse("2026-10-09T10:00:00Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.of("UTC"));

        Promise.setClock(fixedClock);

        try {
            // Profil va servis yaratamiz.
            LifeProfile profile = new LifeProfile("Shaxboz");
            PromiseService service = new PromiseService(profile);

            // Kelajakdagi muddat bilan vazifa yaratamiz.
            Promise promise = new Promise(
                    "Java o'rganish",
                    "OOP mavzusini o'rganish",
                    LocalDateTime.now(fixedClock).plusHours(1),
                    PromiseCategory.STUDY
            );

            service.addPromise(promise);

            // Vazifani boshlaymiz.
            assertTrue(service.startPromise(promise.getId()));

            // Vazifani bajaramiz.
            assertTrue(service.completePromise(promise.getId()));

            // Holati COMPLETED ekanini tekshiramiz.
            assertEquals(PromiseStatus.COMPLETED, promise.getStatus());

            // Vaqtni vazifa muddati o'tgan paytdan keyinga o'tkazamiz.
            Clock laterClock = Clock.fixed(
                    fixedInstant.plusSeconds(2 * 60 * 60),
                    ZoneId.of("UTC")
            );
            Promise.setClock(laterClock);

            // Muddati o'tgan vazifalarni tekshiramiz.
            service.chescOverduePromise();

            // Bajarilgan vazifa COMPLETED bo'lib qolishi kerak.
            assertEquals(PromiseStatus.COMPLETED, promise.getStatus());

            // OVERDUE ro'yxatiga kirmasligi kerak.
            assertTrue(service.getOverduePromises().isEmpty());

        } finally {
            // Boshqa testlarga ta'sir qilmasligi uchun vaqtni tiklaymiz.
            Promise.setClock(Clock.systemDefaultZone());
        }
    }

    @Test
    void updateDeadlineShouldResetOverduePromiseToPending() {
        // Test uchun boshlang'ich vaqtni belgilaymiz.
        Instant fixedInstant = Instant.parse("2026-10-09T10:00:00Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.of("UTC"));

        Promise.setClock(fixedClock);

        try {
            // Profil va servis yaratamiz.
            LifeProfile profile = new LifeProfile("Shaxboz");
            PromiseService service = new PromiseService(profile);

            // Dastlab kelajakdagi muddat bilan vazifa yaratamiz.
            Promise promise = new Promise(
                    "Java o'rganish",
                    "OOP mavzusini o'rganish",
                    LocalDateTime.now(fixedClock).plusHours(1),
                    PromiseCategory.STUDY
            );

            service.addPromise(promise);

            // Vaqtni vazifa muddati o'tgan paytdan keyinga o'tkazamiz.
            Clock laterClock = Clock.fixed(
                    fixedInstant.plusSeconds(2 * 60 * 60),
                    ZoneId.of("UTC")
            );
            Promise.setClock(laterClock);

            // Muddati o'tgan holatga o'tkazamiz.
            service.chescOverduePromise();

            assertEquals(PromiseStatus.OVERDUE, promise.getStatus());

            // Vazifaga yangi muddat beramiz.
            LocalDateTime newDeadline =
                    LocalDateTime.now(laterClock).plusDays(1);

            promise.updateDeadline(newDeadline);

            // Yangi muddat saqlanganini tekshiramiz.
            assertEquals(newDeadline, promise.getDeadline());

            // Status PENDING holatiga qaytishi kerak.
            assertEquals(PromiseStatus.PENDING, promise.getStatus());

        } finally {
            // Haqiqiy vaqtni tiklaymiz.
            Promise.setClock(Clock.systemDefaultZone());
        }
    }

    @Test
    void updateTitleShouldChangePromiseTitle() {
        // Profil yaratamiz
        LifeProfile profile = new LifeProfile("Shaxboz");

        // PromiseService yaratamiz
        PromiseService service = new PromiseService(profile);

        // Yangi vazifa yaratamiz
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz
        service.addPromise(promise);

        // Vazifa nomini yangilaymiz
        promise.updateTitle("Java mashq qilish");

        // Nom o'zgarganini tekshiramiz
        assertEquals("Java mashq qilish", promise.getTitle());
    }

    @Test
    void updateTitleShouldRejectBlankTitle() {
        // Dastlabki nom bilan vazifa yaratamiz
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.STUDY
        );

        // Bo'sh nom berilganda xatolik chiqishini tekshiramiz
        assertThrows(IllegalArgumentException.class, () -> {
            promise.updateTitle("   ");
        });

        // Xatolikdan keyin eski nom saqlanganini tekshiramiz
        assertEquals("java o'rganish", promise.getTitle());
    }


    @Test
    void updateDescriptionShouldChangePromiseDescription() {
        // Yangi vazifa yaratamiz
        Promise promise = new Promise(
                "Java o'rganish",
                "Eski izoh",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.STUDY
        );

        // Vazifa izohini yangilaymiz
        promise.updateDescription("OOP bo'yicha mashqlar bajarish");

        // Yangi izoh saqlanganini tekshiramiz
        assertEquals(
                "OOP bo'yicha mashqlar bajarish",
                promise.getDescription()
        );
    }

    @Test
    void updateDescriptionShouldRejectBlankDescription() {
        // Dastlabki izoh bilan vazifa yaratamiz
        Promise promise = new Promise(
                "Java o'rganish",
                "Eski izoh",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.STUDY
        );

        // Bo'sh izoh berilganda xatolik chiqishi kerak
        assertThrows(IllegalArgumentException.class, () -> {
            promise.updateDescription("   ");
        });

        // Xatolikdan keyin eski izoh saqlanishi kerak
        assertEquals("eski izoh", promise.getDescription());
    }


    @Test
    void updateCategoryShouldChangePromiseCategory() {
        // Dastlab STUDY kategoriyasida vazifa yaratamiz
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.STUDY
        );

        // Kategoriyani PERSONAL ga o'zgartiramiz
        promise.updateCategory(PromiseCategory.PERSONAL);

        // Yangi kategoriya saqlanganini tekshiramiz
        assertEquals(PromiseCategory.PERSONAL, promise.getCategory());
    }


    @Test
    void updateCategoryShouldRejectNullCategory() {
        // Dastlabki kategoriya bilan vazifa yaratamiz
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.STUDY
        );

        // null kategoriya berilganda xatolik chiqishini tekshiramiz
        assertThrows(IllegalArgumentException.class, () -> {
            promise.updateCategory(null);
        });

        // Xatolikdan keyin eski kategoriya saqlanishi kerak
        assertEquals(PromiseCategory.STUDY, promise.getCategory());
    }


    @Test
    void updatePromiseShouldUpdateAllFields() {
        // Foydalanuvchi profilini yaratamiz
        LifeProfile profile = new LifeProfile("Shaxboz");

        // Profil bilan ishlaydigan xizmatni yaratamiz
        PromiseService service = new PromiseService(profile);

        // Dastlabki vazifani yaratamiz
        Promise promise = new Promise(
                "Java o'rganish",
                "Eski izoh",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.STUDY
        );

        // Vazifani profilga qo'shamiz
        service.addPromise(promise);

        // Yangi muddatni belgilaymiz
        LocalDateTime newDeadline = LocalDateTime.now().plusDays(3);

        // Vazifani ID orqali yangilaymiz
        boolean updated = service.updatePromise(
                promise.getId(),
                "Kitob o'qish",
                "20 sahifa o'qish",
                newDeadline,
                PromiseCategory.PERSONAL
        );

        // Yangilash muvaffaqiyatli bo'lganini tekshiramiz
        assertTrue(updated);

        // Har bir maydon o'zgarganini tekshiramiz
        assertEquals("Kitob o'qish", promise.getTitle());
        assertEquals("20 sahifa o'qish", promise.getDescription());
        assertEquals(newDeadline, promise.getDeadline());
        assertEquals(PromiseCategory.PERSONAL, promise.getCategory());
    }


    @Test
    void updatePromiseShouldReturnFalseForUnknownId() {
        // Bo'sh profil yaratamiz
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Mavjud bo'lmagan ID bilan yangilashga urinamiz
        boolean updated = service.updatePromise(
                9999,
                "Yangi nom",
                "Yangi izoh",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // Yangilash amalga oshmaganini tekshiramiz
        assertFalse(updated);
    }



    @Test
    void completePromiseShouldRejectPendingPromise() {
        // Profil va xizmat yaratamiz
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Yangi, hali boshlanmagan vazifa yaratamiz
        Promise promise = new Promise(
                "Kitob o'qish",
                "10 sahifa o'qish",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.PERSONAL
        );

        service.addPromise(promise);

        // Boshlanmagan vazifani yakunlash rad etilishi kerak
        assertFalse(service.completePromise(promise.getId()));

        // Vazifa PENDING holatida qolishi kerak
        assertEquals(PromiseStatus.PENDING, promise.getStatus());
    }


    @Test
    void cancelPromiseShouldRejectCompletedPromise() {
        // Profil va xizmat yaratamiz
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Vazifa yaratamiz
        Promise promise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.STUDY
        );

        service.addPromise(promise);

        // Avval vazifani boshlaymiz
        assertTrue(service.startPromise(promise.getId()));

        // Vazifani yakunlaymiz
        assertTrue(service.completePromise(promise.getId()));

        // Yakunlangan vazifani bekor qilish rad etilishi kerak
        assertFalse(service.cancelPromise(promise.getId()));

        // Holat COMPLETED bo'lib qolishi kerak
        assertEquals(PromiseStatus.COMPLETED, promise.getStatus());
    }


    @Test
    void findPromiseByIdShouldReturnNullForUnknownId() {
        // Profil va xizmat yaratamiz
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Hali hech qanday vazifa qo'shmaymiz

        // Mavjud bo'lmagan ID ni qidiramiz
        Promise found = service.findPromiseById(9999);

        // Natija null bo'lishi kerak
        assertNull(found);
    }
}