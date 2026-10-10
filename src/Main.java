import model.Promise;
import model.LifeProfile;
import model.Priority;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import model.PromiseStatus;
import service.PromiseService;
import util.InputHelper;
import model.PromiseCategory;

public class Main {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        InputHelper input = new InputHelper(scanner);
        LifeProfile profile = new LifeProfile("Shaxboz");

        var promise1 = new Promise(
                "kitob o'qish",
                "10 sahifa",
                LocalDateTime.of(2026,10,12, 18, 30),
                PromiseCategory.STUDY);

        var promise2 = new Promise(
                "darslarni qilish",
                "OOP ni o'rganish",
                LocalDateTime.of(2026, 10, 10, 8, 30).plusDays(1),
                PromiseCategory.STUDY);

        var promise3 = new Promise(
                "Bozorga borish",
                "Atir olish kerak",
                LocalDateTime.of(2026, 10, 13, 10, 0),
                PromiseCategory.OTHER);
        var promise4 = new Promise(
                "Test",
                "Overdue test",
                LocalDateTime.now().plusSeconds(10),
                PromiseCategory.STUDY
        );

//        Promise testPromise = new Promise(
//                "Test vazifa",
//                "Statuslarni tekshirish",
//                LocalDateTime.of(2026, 10, 20, 18, 0),
//                PromiseCategory.STUDY
//        );
//
//        System.out.println("Boshlang'ich: " + testPromise.getStatus());
//
//        System.out.println("Complete: " + testPromise.complete());
//        System.out.println("Status: " + testPromise.getStatus());
//
//        System.out.println("Start: " + testPromise.start());
//        System.out.println("Status: " + testPromise.getStatus());
//
//        System.out.println("Complete: " + testPromise.complete());
//        System.out.println("Status: " + testPromise.getStatus());
//
//        System.out.println("Cancel: " + testPromise.cancel());
//        System.out.println("Status: " + testPromise.getStatus());

        var service = new PromiseService(profile);
        service.addPromise(promise1);
        service.addPromise(promise2);
        service.addPromise(promise3);
        service.addPromise(promise4);
//        service.addPromise(testPromise);

//      Avtomatik overdue ni ishga tushirish
        service.startOverdueChecker();

        while (true) {
            System.out.println();
            System.out.println("===========VAZIFALAR============");
            System.out.println("1. Barcha vazifalar");
            System.out.println("2. Vazifani boshlash");
            System.out.println("3. Vazifani bajarish");
            System.out.println("4. Vazifani bekor qilish");
            System.out.println("5. Vazifani o'chirish");
            System.out.println("6. Vazifani o'zgartirish");
            System.out.println("7. Vazifani qo'shish");
            System.out.println("8. Kategoriyalar bo'yicha ko'rsatish");
            System.out.println("9. Status bo'yicha ko'rsatish");
            System.out.println("10. Dashboard / Statistika");
            System.out.println("11. Deadline yaqinlashayotgan vazifalar");
            System.out.println("12. Muddati o'tgan vazifalar");
            System.out.println("13. Bugungi vazifalar");
            System.out.println("14. Bugungi vazifalarni kategoriya bo‘yicha ko‘rish");
            System.out.println("15. Vazifalarni deadline bo'yicha saralash");
            System.out.println("16. Vazifalarni kategoriya va holat bo'yicha filtrlash");
            System.out.println("17. Vazifani nomi bo'yicha qidirish");
            System.out.println("0. Chiqish");

            String choice = input.readRequiredString("Tanlang: ");
            switch (choice) {
                case "0": //chiqish
                    System.out.println("Dastur tugadi.");
                    service.stopOverdueChecker();
                    return;
                case "1": //Barcha vazifalar
                        showAllPromises(service);
                    break;
                case "2": //Vazifani boshlash
                        startPromise(service, input);
                    break;
                case "3": //Vazifani bajarish
                        completePromise(service, input);
                    break;
                case "4": //Vazifani bekor qilish
                    cancelPromise(service, input);
                    break;
                case "5": //Vazifani o'chirish
                    removePromise(service, input);
                    break;
                case "6": //Vazifani o'zgartirish
                    updatePromise(service, input);
                    break;
                case "7": //Vazifani qo'shish
                    addPromise(service, input);
                    break;
                case "8": //Kategoriya bo'yicha ko'rish
                    showPromisesByCategory(service, input);
                    break;
                case "9": //Status bo'yicha ko'rish
                    showPromiseByStatus(service, input);
                    break;
                case "10": //Statistika
                    showDashboard(service);
                    break;
                case "11": //YAqinlashayotgan dedlinelarni ko'rish
                    showUpcomingPromises(service);
                    break;
                case "12": //YAqinlashayotgan dedlinelarni ko'rish
                    showOverduePromises(service);
                    break;
                case "13": // Bugungi vazifalar
                    showTodayPromises(service);
                    break;
                case "14": // Bugungi vazifalar
                    showTodayPromisesByCategory(service, input);
                    break;
                case "15": // Vazifalarni deadline bo'yicha tartiblash
                    showPromisesSortedByDeadline(service);
                    break;
                case "16": // kategoriya va holati bo'yicha tanlash
                    showPromisesByCategoryAndStatus(service, input);
                    break;
                case "17": // nomi bo'yicha qidirish
                    searchPromisesByTitle(service, input);
                    break;
                default: System.out.println("Noto'g'ri tanlov (0 - 15) oralig'ida tanlang");
            }

            System.out.println("===== Ustuvorlik bo'yicha vazifalar =====");

            for (Promise promise : service.getPromisesSortedByPriority()) {
                System.out.println(
                        promise.getPriority() + " | " + promise
                );
            }
        }
    }



    private static void showAllPromises(PromiseService service) {
        List<Promise> promises = service.getAllPromises();
        if(promises.isEmpty()) {
            System.out.println("Hozircha vazifalar mavjud emas.");
        } else {
            System.out.println("============ BARCHA VAZIFALAR ============\n");
            for (Promise promise : promises) {
                System.out.println(
                        "ID: " + promise.getId() +
                                " | " + "Nomi: " + promise.getTitle() +
                                " | " + "Izoh: " + promise.getDescription() +
                                " | " + "Category: " + promise.getCategory() +
                                " | " + "Deadline: " + promise.getDeadline() +
                                " | " + "Status: " + promise.getStatus()
                );
            }
        }
    }

    private static void startPromise(PromiseService service, InputHelper input) {
        int startId = input.readInt("Qaysi vazifani boshlamoqchisiz: ");
        boolean started = service.startPromise(startId);
        if (started) {
            System.out.println("Vazifa muvaffaqiyatli boshlandi: ");
        } else {
            System.out.println("Vazifani boshlash mumkin emas, yoki bunday ID mavjud emas");
        }
    }

    private static void completePromise(PromiseService service, InputHelper input) {
        int comleteId = input.readInt("Qaysi vazifani bajardingiz: ");
        boolean completed = service.completePromise(comleteId);
        if (completed) {
            System.out.println("Vazifa muvaffaqiyatli bajarildi.");
        } else {
            System.out.println("Vazifani bajarish mumkin emas. " +
                    "Yoki bunday ID mavjud emas");
        }
    }

    private static void cancelPromise(PromiseService service, InputHelper input) {
        int cancelId = input.readInt("Qaysi vazifani bekor qilmoqchisiz: ");
        boolean cancelled = service.cancelPromise(cancelId);
        if(cancelled) {
            System.out.println("Vazifa muvaffaqiyatli bekor qilindi.");
        } else {
            System.out.println("vazifani bekor qilish mumkin emas. " +
                    "yoki bunday ID mavjud emas");
        }
    }

    private static void removePromise(PromiseService service, InputHelper input) {
        int removeId = input.readInt("Qaysi vazifani o'chirishni xohlaysiz: ");
        Promise removed = service.removePromise(removeId);
        if(removed != null) {
            System.out.println("Vazifa muvaffaqiyatli o'chirildi.");
            System.out.println("O'chirilgan vazifa: " + removed.getTitle());
        } else {
            System.out.println("Xatolik: Bunday ID mavjud emas.");
        }
    }


    private static void updatePromise(PromiseService service, InputHelper input) {
        // O'zgartiriladigan vazifaning ID raqamini olamiz.
        int updateId = input.readInt(
                "Qaysi vazifani o'zgartirmoqchisiz: "
        );

        // ID bo'yicha vazifani topamiz.
        Promise promise = service.findPromiseById(updateId);

        // Bunday ID mavjud bo'lmasa, metodni to'xtatamiz.
        if (promise == null) {
            System.out.println(
                    "Xatolik: Bunday ID bilan vazifa mavjud emas."
            );
            return;
        }

        // Vazifaning eski qiymatlarini saqlaymiz.
        String oldTitle = promise.getTitle();
        String oldDescription = promise.getDescription();
        LocalDateTime oldDeadline = promise.getDeadline();
        PromiseCategory oldCategory = promise.getCategory();

        // Yangi qiymatlarni so'raymiz.
        String newTitle = input.readOptionalString(
                "Vazifaning yangi nomi, o'zgartirmasangiz [Enter] ni bosing: "
        );

        String newDescription = input.readOptionalString(
                "Vazifa uchun yangi izoh, o'zgartirmasangiz [Enter] ni bosing: "
        );

        LocalDateTime newDeadline = input.readOptionalDeadline(
                "Yangi deadline, o'zgartirmasangiz [Enter] ni bosing: "
        );

        PromiseCategory newCategory = input.readOptionalCategory(
                "Yangi kategoriya, o'zgartirmasangiz [Enter] ni bosing: "
        );

        // Bo'sh maydonlar uchun eski qiymatlarni saqlaymiz.
        if (newTitle == null || newTitle.isBlank()) {
            newTitle = oldTitle;
        }

        if (newDescription == null || newDescription.isBlank()) {
            newDescription = oldDescription;
        }

        if (newDeadline == null) {
            newDeadline = oldDeadline;
        }

        if (newCategory == null) {
            newCategory = oldCategory;
        }

        // Vazifani faqat bir marta yangilaymiz.
        boolean updated = service.updatePromise(
                updateId,
                newTitle,
                newDescription,
                newDeadline,
                newCategory
        );

        // Natijani foydalanuvchiga ko'rsatamiz.
        if (updated) {
            System.out.println("Vazifa muvaffaqiyatli o'zgartirildi.");
        } else {
            System.out.println(
                    "Xatolik: Vazifani o'zgartirib bo'lmadi."
            );
        }
    }


    private static void addPromise(PromiseService service, InputHelper input) {
        System.out.println("Yangi vazifa qo'shing!");

        try {
            // 1. Vazifa ma'lumotlarini foydalanuvchidan olamiz.
            String title = input.readRequiredString("Vazifa nomini kiriting: ");

            String description = input.readRequiredString("Izohni kiriting: ");

            LocalDateTime deadline = input.readOptionalDeadline("Deadline: ");

            PromiseCategory category = input.readCategory("Vazifa turi: ");

            // 2. Vazifa ustuvorligini tanlaymiz.
            Priority priority = input.readPriority(
                    "Vazifa ustuvorligini tanlang:"
            );

            // 3. Barcha ma'lumotlar bilan bitta vazifa yaratamiz.
            Promise promise = new Promise(
                    title,
                    description,
                    deadline,
                    category
            );

            // 4. Tanlangan ustuvorlikni o'rnatamiz.
            promise.setPriority(priority);

            // 5. Vazifani faqat bir marta qo'shamiz.
            service.addPromise(promise);

            System.out.println("Vazifa muvaffaqiyatli qo'shildi!");

            System.out.println("Deadline: " + promise.getDeadline());
            System.out.println("Priority: " + promise.getPriority());

            // Sinov uchun ustuvorliklarni belgilaymiz.
            Promise javaPromise = service.findPromise("java");
            if (javaPromise != null) {
                javaPromise.setPriority(Priority.HIGH);
            }

            Promise bookPromise = service.findPromise("kitob o'qish");
            if (bookPromise != null) {
                bookPromise.setPriority(Priority.LOW);
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Xatolik: " + e.getMessage());
        }
    }

    private static void showPromisesByCategory(PromiseService service, InputHelper input) {
        PromiseCategory category = input.readOptionalCategory("Qaysi kategoriyani ko'rishni xohlaysiz?");
        List<Promise> promises = service.findPromisesByCategory(category);
        if(promises.isEmpty()) {
            System.out.println("Bu kategoriyada vazifa mavjud emas.");
            return;
        }
        System.out.println("=============" + category + " Vazifalari =============");
        for (Promise promise : promises) {
            System.out.println("ID: " + promise.getId() +
                    " | " + "Nomi: " + promise.getTitle() +
                    " | " + "Izoh: " + promise.getDescription() +
                    " | " + "Kategoriya: " + promise.getCategory() +
                    " | " + "Deadline: " + promise.getDeadline() +
                    " | " + "Status: " + promise.getStatus());

        }
    }

    private static void showPromiseByStatus(PromiseService service, InputHelper input) {
        PromiseStatus status = input.readStatus("Qaysi status malumotlarini korishni xohlaysiz? ");
        List<Promise> promises = service.findPromisesByStatus(status);
        if(promises.isEmpty()) {
            System.out.println("Bu statusda vazifalar mavjud emas");
            return;
        }
        System.out.println("============== " + status + " vazifalari ==============");
        for (Promise promise : promises) {
            System.out.println("ID: " + promise.getId() +
                    " | " + "Nomi: " + promise.getTitle() +
                    " | " + "Izoh: " + promise.getDescription() +
                    " | " + "Kategoriya: " + promise.getCategory() +
                    " | " + "Deadline: " + promise.getDeadline() +
                    " | " + "Status: " + promise.getStatus());
        }
    }

    private static void showDashboard(PromiseService service) {
        int total = service.getAllPromises().size();
        System.out.println();
        System.out.println("========= LIFE NAVIGATOR ==========");
        System.out.println("Jami vazifalar: " + total);
        System.out.println("Kutilmoqda: " + service.countPromisesByStatus(PromiseStatus.PENDING));
        System.out.println("Jarayonda: " + service.countPromisesByStatus(PromiseStatus.IN_PROGRESS));
        System.out.println("Bajarilgan: " + service.countPromisesByStatus(PromiseStatus.COMPLETED));
        System.out.println("Bekor qilingan: " + service.countPromisesByStatus(PromiseStatus.CANCELLED));
        System.out.println("Muddati o'tgan: " + service.countPromisesByStatus(PromiseStatus.OVERDUE));
        System.out.println("===================================");
    }

    private static void showUpcomingPromises(PromiseService service) {
        List<Promise> promises = service.getUpcomingPromise();
        System.out.println("======== DEADLINE YAQINLASHAYOTGAN VAZIFALAR ========");
        if (promises.isEmpty()) {
            System.out.println("Kiyingi 24 soat ichida deadline yo'q.");
            return;
        }
        for (Promise promise : promises) {
            System.out.println("ID: " + promise.getId() +
                    " | Nomi: " + promise.getTitle() +
                    " | Deadline: " + promise.getDeadline() +
                    " | Status: " + promise.getStatus()
            );
        }
    }

    private static void showOverduePromises(PromiseService service) {

        // Service orqali muddati o'tgan vazifalarni olamiz.
        List<Promise> promises = service.getOverduePromises();

        // Bo'lim sarlavhasini chiqaramiz.
        System.out.println("======== MUDDATI O'TGAN VAZIFALAR ========");

        // Agar ro'yxat bo'sh bo'lsa, foydalanuvchiga xabar beramiz.
        if (promises.isEmpty()) {
            System.out.println("Muddati o'tgan vazifalar mavjud emas.");
            return;
        }

        // Har bir muddati o'tgan vazifa haqida ma'lumot chiqaramiz.
        for (Promise promise : promises) {
            System.out.println(
                    "ID: " + promise.getId()
                            + " | Nomi: " + promise.getTitle()
                            + " | Izoh: " + promise.getDescription()
                            + " | Deadline: " + promise.getDeadline()
                            + " | Status: " + promise.getStatus()
            );
        }
    }

    private static void showTodayPromises(PromiseService service) {

        // Service orqali bugungi vazifalarni olamiz.
        List<Promise> promises = service.getTodayPromises();

        // Bo'lim sarlavhasini chiqaramiz.
        System.out.println("======== BUGUNGI VAZIFALAR ========");

        // Bugungi vazifalar bo'lmasa, xabar chiqaramiz.
        if (promises.isEmpty()) {
            System.out.println("Bugun muddati keladigan vazifalar yo'q.");
            return;
        }

        // Har bir vazifaning ma'lumotlarini chiqaramiz.
        for (Promise promise : promises) {
            System.out.println(
                    "ID: " + promise.getId()
                            + " | Nomi: " + promise.getTitle()
                            + " | Deadline: " + promise.getDeadline()
                            + " | Status: " + promise.getStatus()
            );
        }
    }

    private static void showTodayPromisesByCategory(PromiseService service, InputHelper input) {

        // Foydalanuvchidan kategoriyani tanlashni so'raymiz.
        PromiseCategory category = input.readCategory(
                "Kategoriyani tanlang (STUDY, WORK, HEALTH, PERSONAL, OTHER): "
        );

        // Tanlangan kategoriyadagi bugungi vazifalarni olamiz.
        List<Promise> promises =
                service.getTodayPromisesByCategory(category);

        // Hech qanday vazifa topilmasa, xabar chiqaramiz.
        if (promises.isEmpty()) {
            System.out.println("Bu kategoriyada bugungi vazifalar yo'q.");
            return;
        }

        // Topilgan vazifalarni ekranga chiqaramiz.
        System.out.println("===== Bugungi vazifalar: " + category + " =====");

        for (Promise promise : promises) {
            System.out.println(promise);
        }
    }

    private static void showPromisesSortedByDeadline(PromiseService service) {

        // Vazifalarni deadline bo'yicha saralangan holda olamiz.
        List<Promise> promises = service.getPromisesSortedByDeadline();

        // Ro'yxat bo'sh bo'lsa, xabar chiqaramiz.
        if (promises.isEmpty()) {
            System.out.println("Hozircha vazifalar mavjud emas.");
            return;
        }

        // Saralangan vazifalar sarlavhasini chiqaramiz.
        System.out.println("===== Vazifalar: eng yaqin deadline birinchi =====");

        // Har bir vazifani navbat bilan ekranga chiqaramiz.
        for (Promise promise : promises) {
            System.out.println(promise);
        }
    }

//    private static void showPromisesByCategoryAndStatus(PromiseService service, InputHelper input) {
//
//        // Foydalanuvchidan kategoriyani tanlashni so'raymiz.
//        PromiseCategory category = input.readCategory(
//                "Kategoriyani tanlang: "
//        );
//
//        // Foydalanuvchidan vazifa holatini tanlashni so'raymiz.
//        PromiseStatus status = input.readStatus(
//                "Vazifa holatini tanlang: "
//        );
//
//        // Tanlangan kategoriya va holat bo'yicha vazifalarni olamiz.
//        List<Promise> promises =
//                service.getPromisesByCategoryAndStatus(category, status);
//
//        // Mos vazifalar topilmasa, xabar chiqaramiz.
//        if (promises.isEmpty()) {
//            System.out.println("Bu kategoriya va holatga mos vazifalar topilmadi.");
//            return;
//        }
//
//        // Natijalar sarlavhasini chiqaramiz.
//        System.out.println("===== Filtrlangan vazifalar =====");
//        System.out.println("Kategoriya: " + category);
//        System.out.println("Holat: " + status);
//
//        // Har bir topilgan vazifani ekranga chiqaramiz.
//        for (Promise promise : promises) {
//            System.out.println(promise);
//        }
//    }

    private static void showPromisesByCategoryAndStatus(PromiseService service, InputHelper input) {

        // 1. Kategoriyani tanlaymiz.
        PromiseCategory category = input.readCategory(
                "Avval kategoriyani tanlang:"
        );

        // 2. Shu kategoriyadagi barcha vazifalarni chiqaramiz.
        List<Promise> categoryPromises = service.getPromisesByCategory(category);

        System.out.println("===== " + category + " kategoriyasidagi vazifalar =====");

        if (categoryPromises.isEmpty()) {
            System.out.println("Bu kategoriyada vazifalar mavjud emas.");
            return;
        }

        for (Promise promise : categoryPromises) {
            System.out.println(
                    "ID: " + promise.getId() +
                            " | " + "Nomi: " + promise.getTitle() +
                            " | " + "Izoh: " + promise.getDescription() +
                            " | " + "Category: " + promise.getCategory() +
                            " | " + "Deadline: " + promise.getDeadline() +
                            " | " + "Status: " + promise.getStatus()
            );
        }

        // 3. Endi foydalanuvchidan holatni tanlashni so'raymiz.
        PromiseStatus status = input.readStatus(
                "Endi kerakli holatni tanlang:"
        );

        // 4. Tanlangan kategoriya va holat bo'yicha filtrlash.
        List<Promise> filteredPromises =
                service.getPromisesByCategoryAndStatus(category, status);

        // 5. Yakuniy natijani chiqaramiz.
        System.out.println("===== Tanlangan kategoriya va holat bo'yicha natija =====");

        if (filteredPromises.isEmpty()) {
            System.out.println("Bu kategoriyada tanlangan holatga mos vazifalar yo'q.");
            return;
        }

        for (Promise promise : filteredPromises) {
            System.out.println(
                    "ID: " + promise.getId() +
                            " | " + "Nomi: " + promise.getTitle() +
                            " | " + "Izoh: " + promise.getDescription() +
                            " | " + "Category: " + promise.getCategory() +
                            " | " + "Deadline: " + promise.getDeadline() +
                            " | " + "Status: " + promise.getStatus()
            );
        }
    }

    private static void searchPromisesByTitle(PromiseService service, InputHelper input) {

        // Foydalanuvchidan qidiruv so'zini olamiz.
        String keyword = input.readRequiredString(
                "Qidirilayotgan vazifa yoki izohini kiriting: "
        );

        // Vazifalarni nomining bir qismi orqali qidiramiz.
        List<Promise> results =
                service.searchPromises(keyword);

        // Natijalarni chiqaramiz.
        if (results.isEmpty()) {
            System.out.println("Mos vazifa topilmadi.");
            return;
        }

        System.out.println("===== Qidiruv natijalari =====");

        for (Promise promise : results) {
            System.out.println(
                    "ID: " + promise.getId() +
                            " | " + "Nomi: " + promise.getTitle() +
                            " | " + "Izoh: " + promise.getDescription() +
                            " | " + "Category: " + promise.getCategory() +
                            " | " + "Deadline: " + promise.getDeadline() +
                            " | " + "Status: " + promise.getStatus()
            );
        }
    }



}
