
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


    @Test
    void removePromiseShouldRemovePromiseById() {
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

        // Vazifani profilga qo'shamiz
        service.addPromise(promise);

        // Vazifa ro'yxatga qo'shilganini tekshiramiz
        assertSame(promise, service.findPromiseById(promise.getId()));

        // Vazifani ID orqali o'chiramiz
        Promise removed = service.removePromise(promise.getId());

        // O'chirilgan obyekt aynan shu vazifa ekanini tekshiramiz
        assertSame(promise, removed);

        // Vazifa endi topilmasligi kerak
        assertNull(service.findPromiseById(promise.getId()));

        // Ro'yxat bo'sh qolganini tekshiramiz
        assertTrue(service.getAllPromises().isEmpty());
    }


    @Test
    void removePromiseShouldReturnNullForUnknownId() {
        // Profil va xizmat yaratamiz
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Mavjud bo'lmagan ID ni o'chirishga urinib ko'ramiz
        Promise removed = service.removePromise(9999);

        // Hech qanday vazifa o'chirilmagan bo'lishi kerak
        assertNull(removed);

        // Ro'yxat o'zgarmagan va bo'sh qolgan bo'lishi kerak
        assertTrue(service.getAllPromises().isEmpty());
    }


    @Test
    void findPendingPromisesShouldReturnOnlyPendingOnes() {
        // Profil va xizmat yaratamiz
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Birinchi vazifa: PENDING
        Promise firstPromise = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.STUDY
        );

        // Ikkinchi vazifa: PENDING
        Promise secondPromise = new Promise(
                "Kitob o'qish",
                "10 sahifa o'qish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.PERSONAL
        );

        // Uchinchi vazifa: IN_PROGRESS bo'ladi
        Promise thirdPromise = new Promise(
                "Mashq qilish",
                "Java kod yozish",
                LocalDateTime.now().plusDays(3),
                PromiseCategory.STUDY
        );

        // Vazifalarni profilga qo'shamiz
        service.addPromise(firstPromise);
        service.addPromise(secondPromise);
        service.addPromise(thirdPromise);

        // Uchinchi vazifani boshlaymiz
        assertTrue(service.startPromise(thirdPromise.getId()));

        // Faqat PENDING holatidagi vazifalarni olamiz
        List<Promise> result =
                service.findPromisesByStatus(PromiseStatus.PENDING);

        // Natijada ikkita vazifa bo'lishi kerak
        assertEquals(2, result.size());

        // To'g'ri vazifalar qaytganini tekshiramiz
        assertTrue(result.contains(firstPromise));
        assertTrue(result.contains(secondPromise));

        // IN_PROGRESS vazifasi natijaga kirmasligi kerak
        assertFalse(result.contains(thirdPromise));
    }


    @Test
    void findStudyPromisesShouldReturnOnlyStudyCategory() {
        // Profil va xizmat yaratamiz
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // STUDY kategoriyasidagi birinchi vazifa
        Promise studyPromise1 = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.STUDY
        );

        // STUDY kategoriyasidagi ikkinchi vazifa
        Promise studyPromise2 = new Promise(
                "Test yozish",
                "JUnit bilan ishlash",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        // PERSONAL kategoriyasidagi vazifa
        Promise personalPromise = new Promise(
                "Kitob o'qish",
                "10 sahifa o'qish",
                LocalDateTime.now().plusDays(3),
                PromiseCategory.PERSONAL
        );

        // Barcha vazifalarni profilga qo'shamiz
        service.addPromise(studyPromise1);
        service.addPromise(studyPromise2);
        service.addPromise(personalPromise);

        // Faqat STUDY kategoriyasidagi vazifalarni qidiramiz
        List<Promise> result =
                service.findPromisesByCategory(PromiseCategory.STUDY);

        // Natijada ikkita vazifa bo'lishi kerak
        assertEquals(2, result.size());

        // Ikkala STUDY vazifasi ham natijada mavjudligini tekshiramiz
        assertTrue(result.contains(studyPromise1));
        assertTrue(result.contains(studyPromise2));

        // PERSONAL vazifasi natijaga kirmasligi kerak
        assertFalse(result.contains(personalPromise));
    }


    @Test
    void countPendingPromisesShouldReturnCorrectCount() {
        // Profil va xizmat yaratamiz
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Birinchi vazifa: PENDING
        Promise promise1 = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.STUDY
        );

        // Ikkinchi vazifa: PENDING
        Promise promise2 = new Promise(
                "Kitob o'qish",
                "10 sahifa o'qish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.PERSONAL
        );

        // Uchinchi vazifa: keyin IN_PROGRESS bo'ladi
        Promise promise3 = new Promise(
                "Test yozish",
                "JUnit bilan ishlash",
                LocalDateTime.now().plusDays(3),
                PromiseCategory.STUDY
        );

        // Vazifalarni profilga qo'shamiz
        service.addPromise(promise1);
        service.addPromise(promise2);
        service.addPromise(promise3);

        // Uchinchi vazifani boshlaymiz
        assertTrue(service.startPromise(promise3.getId()));

        // PENDING vazifalar sonini hisoblaymiz
        int pendingCount =
                service.countPromisesByStatus(PromiseStatus.PENDING);

        // Faqat ikkita vazifa PENDING bo'lishi kerak
        assertEquals(2, pendingCount);

        // IN_PROGRESS vazifalar sonini ham tekshiramiz
        int inProgressCount =
                service.countPromisesByStatus(PromiseStatus.IN_PROGRESS);

        assertEquals(1, inProgressCount);
    }


    @Test
    void upcomingListShouldContainOnlyActivePromises() {
        // Profil va xizmat yaratamiz
        LifeProfile profile = new LifeProfile("Shaxboz");
        PromiseService service = new PromiseService(profile);

        // Birinchi vazifa: PENDING
        Promise promise1 = new Promise(
                "Java o'rganish",
                "OOP mavzusini o'rganish",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.STUDY
        );

        // Ikkinchi vazifa: PENDING
        Promise promise2 = new Promise(
                "Kitob o'qish",
                "10 sahifa o'qish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.PERSONAL
        );

        // Uchinchi vazifa: keyin IN_PROGRESS bo'ladi
        Promise promise3 = new Promise(
                "Test yozish",
                "JUnit bilan ishlash",
                LocalDateTime.now().plusDays(3),
                PromiseCategory.STUDY
        );

        // Vazifalarni profilga qo'shamiz
        service.addPromise(promise1);
        service.addPromise(promise2);
        service.addPromise(promise3);

        // Uchinchi vazifani boshlaymiz
        assertTrue(service.startPromise(promise3.getId()));

        // PENDING vazifalar sonini hisoblaymiz
        int pendingCount =
                service.countPromisesByStatus(PromiseStatus.PENDING);

        // Faqat ikkita vazifa PENDING bo'lishi kerak
        assertEquals(2, pendingCount);

        // IN_PROGRESS vazifalar sonini ham tekshiramiz
        int inProgressCount =
                service.countPromisesByStatus(PromiseStatus.IN_PROGRESS);

        assertEquals(1, inProgressCount);
    }


    @Test
    void updatePromiseShouldPreserveOldDataWhenDeadlineIsInvalid() {
        // Profil va servis yaratamiz
        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        // Kelajakdagi muddat bilan va'da yaratamiz
        LocalDateTime oldDeadline = LocalDateTime.now().plusDays(2);

        Promise promise = new Promise(
                "Java",
                "Eski izoh",
                oldDeadline,
                PromiseCategory.STUDY
        );

        service.addPromise(promise);

        // Eski qiymatlarni saqlab olamiz
        String oldTitle = promise.getTitle();
        String oldDescription = promise.getDescription();
        LocalDateTime savedDeadline = promise.getDeadline();

        // O'tmishdagi muddat bilan yangilashni tekshiramiz
        LocalDateTime pastDeadline = LocalDateTime.now().minusDays(1);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.updatePromise(
                        promise.getId(),
                        "Yangi nom",
                        "Yangi izoh",
                        pastDeadline,
                        PromiseCategory.PERSONAL
                )
        );

        // Xato yuz bergach eski ma'lumotlar saqlanganini tekshiramiz
        assertEquals(oldTitle, promise.getTitle());
        assertEquals(oldDescription, promise.getDescription());
        assertEquals(savedDeadline, promise.getDeadline());
        assertEquals(PromiseCategory.STUDY, promise.getCategory());
    }

    @Test
    void updatePromiseShouldKeepOldValuesForNullFields() {
        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        LocalDateTime deadline = LocalDateTime.now().plusDays(2);

        Promise promise = new Promise(
                "Java",
                "Eski izoh",
                deadline,
                PromiseCategory.STUDY
        );

        service.addPromise(promise);

        boolean result = service.updatePromise(
                promise.getId(),
                null,       // Nom o'zgarmasin
                null,       // Izoh o'zgarmasin
                null,       // Muddat o'zgarmasin
                null        // Kategoriya o'zgarmasin
        );

        assertTrue(result);
        assertEquals("java", promise.getTitle());
        assertEquals("eski izoh", promise.getDescription());
        assertEquals(deadline, promise.getDeadline());
        assertEquals(PromiseCategory.STUDY, promise.getCategory());
    }


    @Test
    void updatingUnknownPromiseShouldNotChangeExistingPromises() {
        // Profil va servis yaratamiz
        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        // Mavjud va'dani yaratamiz
        Promise promise = new Promise(
                "Java",
                "OOP o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        service.addPromise(promise);

        // Mavjud bo'lmagan ID orqali yangilashga urinamiz
        boolean result = service.updatePromise(
                999999,
                "Yangi nom",
                "Yangi izoh",
                LocalDateTime.now().plusDays(3),
                PromiseCategory.PERSONAL
        );

        // Yangilash muvaffaqiyatsiz bo'lishi kerak
        assertFalse(result);

        // Mavjud va'da o'zgarmaganini tekshiramiz
        assertEquals("java", promise.getTitle());
        assertEquals("oop o'rganish", promise.getDescription());
        assertEquals(PromiseCategory.STUDY, promise.getCategory());

        // Profil ichida faqat bitta va'da qolishi kerak
        assertEquals(1, service.getAllPromises().size());
    }


    @Test
    void removingSamePromiseTwiceShouldReturnNullSecondTime() {
        // Profil va servis yaratamiz
        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        // Yangi va'da yaratamiz
        Promise promise = new Promise(
                "Java",
                "OOP o'rganish",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.STUDY
        );

        service.addPromise(promise);

        // Birinchi marta o'chiramiz
        Promise removed = service.removePromise(promise.getId());

        // Birinchi urinishda va'da qaytishi kerak
        assertNotNull(removed);
        assertEquals(promise.getId(), removed.getId());

        // Ikkinchi marta o'chirishga urinib ko'ramiz
        Promise removedAgain = service.removePromise(promise.getId());

        // Va'da allaqachon o'chirilgan, shu sababli null kutiladi
        assertNull(removedAgain);

        // Profil bo'sh qolishi kerak
        assertTrue(service.getAllPromises().isEmpty());
    }


    @Test
    void overdueCheckerShouldMarkPromiseAsOverdue() throws InterruptedException {
        // Profil va servis yaratamiz
        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        // Kelajakdagi muddat bilan va'da yaratamiz
        Promise promise = new Promise(
                "Java",
                "OOP o'rganish",
                LocalDateTime.now().plusSeconds(2),
                PromiseCategory.STUDY
        );

        service.addPromise(promise);

        try {
            // Avtomatik tekshiruvni boshlaymiz
            service.startOverdueChecker();

            // Muddat o'tishini kutamiz
            Thread.sleep(3000);

            // Holat OVERDUE bo'lishi kerak
            assertEquals(PromiseStatus.OVERDUE, promise.getStatus());
        } finally {
            // Test tugaganda scheduler'ni to'xtatamiz
            service.stopOverdueChecker();
        }
    }


    @Test
    void completedAndCancelledPromisesShouldNotBecomeOverdue() {
        // Sinov vaqtini belgilaymiz
        Clock originalClock = Clock.systemDefaultZone();
        Clock fixedClock = Clock.fixed(
                Instant.parse("2026-10-09T10:00:00Z"),
                ZoneId.of("UTC")
        );

        Promise.setClock(fixedClock);

        try {
            // Profil va servis yaratamiz
            LifeProfile profile = new LifeProfile("Test");
            PromiseService service = new PromiseService(profile);

            // Hozirgi sinov vaqtidan keyingi muddat
            LocalDateTime deadline = LocalDateTime.now().plusHours(1);

            Promise completed = new Promise(
                    "Kitob",
                    "Kitob o'qish",
                    deadline,
                    PromiseCategory.PERSONAL
            );

            Promise cancelled = new Promise(
                    "Sport",
                    "Sport bilan shug'ullanish",
                    deadline,
                    PromiseCategory.PERSONAL
            );

            service.addPromise(completed);
            service.addPromise(cancelled);

            // Birinchi va'dani bajaramiz
            assertTrue(completed.start());
            assertTrue(completed.complete());

            // Ikkinchi va'dani bekor qilamiz
            assertTrue(cancelled.cancel());

            // Vaqtni muddatdan keyinga o'tkazamiz
            Promise.setClock(Clock.fixed(
                    Instant.parse("2026-10-09T12:00:00Z"),
                    ZoneId.of("UTC")
            ));

            // Muddatlarni tekshiramiz
            completed.checkOverdue();
            cancelled.checkOverdue();

            // Holatlar o'zgarmasligi kerak
            assertEquals(PromiseStatus.COMPLETED, completed.getStatus());
            assertEquals(PromiseStatus.CANCELLED, cancelled.getStatus());

        } finally {
            // Boshqa testlarga ta'sir qilmasligi uchun soatni tiklaymiz
            Promise.setClock(originalClock);
        }
    }


    @Test
    void stoppingOverdueCheckerShouldShutdownScheduler() {
        // Profil va servis yaratamiz
        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        // Avtomatik tekshiruvni boshlaymiz
        service.startOverdueChecker();

        // Tekshiruvchini to'xtatamiz
        service.stopOverdueChecker();

        // Scheduler yopilganini tekshiramiz
        assertTrue(service.isOverdueCheckerShutdown());
    }


    @Test
    void startingStoppedOverdueCheckerShouldThrowException() {

        // Test uchun profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        // Scheduler'ni avval ishga tushiramiz.
        service.startOverdueChecker();

        // Endi uni to'xtatamiz.
        service.stopOverdueChecker();

        // To'xtatilgan scheduler'ni qayta ishga tushirish
        // IllegalStateException chiqarishi kerak.
        assertThrows(
                IllegalStateException.class,
                service::startOverdueChecker
        );
    }

    @Test
    void startingOverdueCheckerTwiceShouldThrowException() {
        // Scheduler uchun kerakli obyektlarni yaratamiz.
        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        // Birinchi marta ishga tushirish muvaffaqiyatli bo'lishi kerak.
        service.startOverdueChecker();

        // Ikkinchi marta ishga tushirish exception chiqarishi kerak.
        assertThrows(
                IllegalStateException.class,
                service::startOverdueChecker
        );

        // Testdan keyin scheduler'ni to'xtatamiz.
        service.stopOverdueChecker();
    }

    @Test
    void stoppingOverdueCheckerBeforeStartingShouldThrowException() {

        // Test uchun profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        // Ishga tushirilmagan scheduler'ni to'xtatish
        // IllegalStateException chiqarishi kerak.
        assertThrows(
                IllegalStateException.class,
                service::stopOverdueChecker
        );
    }

    @Test
    void overdueCheckerShouldContinueAfterOnePromiseFails() {

        // Profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        // Xatolikni boshqariladigan tarzda sinash uchun
        // Promise klassidagi checkOverdue() metodini
        // alohida almashtirish imkoniyati kerak bo'ladi.
    }

    @Test
    void upcomingPromisesShouldBeSortedByNearestDeadline() {
        // Test uchun hozirgi vaqtni belgilaymiz.
        LocalDateTime now = Promise.getCurrentTime();

        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        // Va'dalarni ataylab noto'g'ri tartibda qo'shamiz.
        Promise later = new Promise(
                "keyinroq", "Ikkinchi vazifa",
                now.plusHours(5), PromiseCategory.PERSONAL
        );

        Promise sooner = new Promise(
                "tezroq", "Birinchi vazifa",
                now.plusHours(2), PromiseCategory.STUDY
        );

        service.addPromise(later);
        service.addPromise(sooner);

        // Natija eng yaqin muddatdan boshlanishi kerak.
        List<Promise> result = service.getUpcomingPromises();

        assertEquals(2, result.size());
        assertEquals(sooner.getId(), result.get(0).getId());
        assertEquals(later.getId(), result.get(1).getId());
    }

    @Test
    void categoryFilterShouldReturnOnlyStudyPromises() {

        // Test uchun profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        // Ikki xil kategoriyadagi va'dalarni yaratamiz.
        LocalDateTime now = Promise.getCurrentTime();

        Promise studyPromise = new Promise(
                "Java o'rganish",
                "OOP mashq qilish",
                now.plusHours(3),
                PromiseCategory.STUDY
        );

        Promise personalPromise = new Promise(
                "Sayr qilish",
                "Toza havoda yurish",
                now.plusHours(5),
                PromiseCategory.PERSONAL
        );

        // Ikkala va'dani profilga qo'shamiz.
        service.addPromise(studyPromise);
        service.addPromise(personalPromise);

        // Faqat STUDY kategoriyasidagi va'dalarni olamiz.
        List<Promise> result =
                profile.findPromisesByCategory(PromiseCategory.STUDY);

        // Natijada faqat bitta va'da bo'lishi kerak.
        assertEquals(1, result.size());

        // Qaytgan va'da aynan studyPromise bo'lishi kerak.
        assertEquals(studyPromise.getId(), result.get(0).getId());

        // Kategoriya ham STUDY bo'lishi kerak.
        assertEquals(PromiseCategory.STUDY, result.get(0).getCategory());
    }

    @Test
    void statusFilterShouldReturnOnlyPendingPromises() {

        // Test uchun profil va servis yaratamiz.
        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        // Kelajakdagi muddatlarni belgilaymiz.
        LocalDateTime now = Promise.getCurrentTime();

        // Birinchi va'da PENDING holatida yaratiladi.
        Promise pendingPromise = new Promise(
                "Kitob o'qish",
                "10 sahifa o'qish",
                now.plusHours(2),
                PromiseCategory.PERSONAL
        );

        // Ikkinchi va'dani yaratamiz.
        Promise inProgressPromise = new Promise(
                "Java o'rganish",
                "OOP mashq qilish",
                now.plusHours(4),
                PromiseCategory.STUDY
        );

        // Ikkinchi va'dani IN_PROGRESS holatiga o'tkazamiz.
        inProgressPromise.start();

        // Ikkala va'dani profilga qo'shamiz.
        service.addPromise(pendingPromise);
        service.addPromise(inProgressPromise);

        // Faqat PENDING va'dalarni topamiz.
        List<Promise> result =
                profile.findPromisesByStatus(PromiseStatus.PENDING);

        // Faqat bitta va'da qaytishi kerak.
        assertEquals(1, result.size());

        // Qaytgan va'da pendingPromise bo'lishi kerak.
        assertEquals(pendingPromise.getId(), result.get(0).getId());

        // Uning statusi PENDING bo'lishi kerak.
        assertEquals(PromiseStatus.PENDING, result.get(0).getStatus());
    }

    @Test
    void getTodayPromisesShouldReturnOnlyPromisesDueToday() {
        // Test vaqtini belgilaymiz.
        Promise.setClock(
                java.time.Clock.fixed(
                        java.time.Instant.parse("2026-10-09T10:00:00Z"),
                        java.time.ZoneOffset.UTC
                )
        );

        try {
            // Test uchun profil va service yaratamiz.
            LifeProfile profile = new LifeProfile("Test");
            PromiseService service = new PromiseService(profile);

            // Bugun muddati keladigan vazifa.
            Promise today = new Promise(
                    "Bugungi vazifa",
                    "Bugun bajarish",
                    LocalDateTime.of(2026, 10, 9, 18, 0),
                    PromiseCategory.STUDY
            );

            // Ertaga muddati keladigan vazifa.
            Promise tomorrow = new Promise(
                    "Ertangi vazifa",
                    "Ertaga bajarish",
                    LocalDateTime.of(2026, 10, 10, 18, 0),
                    PromiseCategory.PERSONAL
            );

            // Ikkala vazifani ham profilga qo'shamiz.
            service.addPromise(today);
            service.addPromise(tomorrow);

            // Bugungi vazifalarni olamiz.
            List<Promise> result = service.getTodayPromises();

            // Faqat bugungi vazifa chiqishi kerak.
            assertEquals(1, result.size());
            assertEquals(today.getId(), result.get(0).getId());
        } finally {
            // Boshqa testlarga ta'sir qilmasligi uchun vaqtni tiklaymiz.
            Promise.setClock(java.time.Clock.systemDefaultZone());
        }
    }

    @Test
    void getTodayPromisesByCategoryShouldReturnOnlyMatchingCategory() {

        // Test vaqtini belgilaymiz.
        Promise.setClock(
                java.time.Clock.fixed(
                        java.time.Instant.parse("2026-10-10T05:00:00Z"),
                        java.time.ZoneOffset.UTC
                )
        );

        try {
            // Test uchun profil va servis yaratamiz.
            LifeProfile profile = new LifeProfile("Test");
            PromiseService service = new PromiseService(profile);

            // Bugungi STUDY vazifasini yaratamiz.
            Promise studyPromise = new Promise(
                    "Java o'rganish",
                    "OOP mashqi",
                    LocalDateTime.of(2026, 10, 10, 18, 0),
                    PromiseCategory.STUDY
            );

            // Bugungi PERSONAL vazifasini yaratamiz.
            Promise personalPromise = new Promise(
                    "Kitob o'qish",
                    "10 sahifa",
                    LocalDateTime.of(2026, 10, 10, 19, 0),
                    PromiseCategory.PERSONAL
            );

            // Ikkala vazifani servisga qo'shamiz.
            service.addPromise(studyPromise);
            service.addPromise(personalPromise);

            // Faqat STUDY kategoriyasini so'raymiz.
            List<Promise> result =
                    service.getTodayPromisesByCategory(PromiseCategory.STUDY);

            // Faqat bitta vazifa qaytishi kerak.
            assertEquals(1, result.size());

            // Qaytgan vazifa aynan STUDY vazifasi bo'lishi kerak.
            assertEquals(studyPromise.getId(), result.get(0).getId());

        } finally {
            // Test tugagach, soatni asl holatiga qaytaramiz.
            Promise.setClock(java.time.Clock.systemDefaultZone());
        }
    }

    @Test
    void getPromisesSortedByDeadlineShouldReturnNearestFirst() {
        // Sinov uchun profil va xizmat yaratamiz.
        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        // Keyinroq bajariladigan vazifani birinchi yaratamiz.
        Promise laterPromise = new Promise(
                "Keyingi vazifa",
                "Keyin bajariladi",
                LocalDateTime.now().plusDays(3),
                PromiseCategory.STUDY
        );

        // Oldinroq bajariladigan vazifani ikkinchi yaratamiz.
        Promise soonerPromise = new Promise(
                "Yaqin vazifa",
                "Oldin bajariladi",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.PERSONAL
        );

        // Vazifalarni xizmatga qo'shamiz.
        service.addPromise(laterPromise);
        service.addPromise(soonerPromise);

        // Saralangan ro'yxatni olamiz.
        List<Promise> result = service.getPromisesSortedByDeadline();

        // Eng yaqin muddatli vazifa birinchi kelishini tekshiramiz.
        assertEquals(soonerPromise.getId(), result.get(0).getId());

        // Eng uzoq muddatli vazifa ikkinchi kelishini tekshiramiz.
        assertEquals(laterPromise.getId(), result.get(1).getId());
    }


    @Test
    void getPromisesByCategoryAndStatusShouldReturnMatchingPromises() {

        // Test uchun profil va xizmat yaratamiz.
        LifeProfile profile = new LifeProfile("Test");
        PromiseService service = new PromiseService(profile);

        // STUDY kategoriyasidagi vazifa yaratamiz.
        Promise studyPromise = new Promise(
                "Java o'rganish",
                "OOP mashqi",
                LocalDateTime.now().plusDays(1),
                PromiseCategory.STUDY
        );

        // PERSONAL kategoriyasidagi vazifa yaratamiz.
        Promise personalPromise = new Promise(
                "Kitob o'qish",
                "10 sahifa",
                LocalDateTime.now().plusDays(2),
                PromiseCategory.PERSONAL
        );

        // Ikkala vazifani xizmatga qo'shamiz.
        service.addPromise(studyPromise);
        service.addPromise(personalPromise);

        // STUDY va PENDING bo'yicha filtrlaymiz.
        List<Promise> result = service.getPromisesByCategoryAndStatus(
                PromiseCategory.STUDY,
                PromiseStatus.PENDING
        );

        // Faqat bitta vazifa topilganini tekshiramiz.
        assertEquals(1, result.size());

        // Topilgan vazifa aynan studyPromise ekanini tekshiramiz.
        assertEquals(studyPromise.getId(), result.get(0).getId());
    }


}