
package service;
import model.LifeProfile;
import model.Promise;
import model.PromiseCategory;
import model.PromiseStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Comparator;


public class PromiseService {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    // Scheduler oldin ishga tushirilganmi?
    private boolean overdueCheckerStarted = false;
    private final LifeProfile profile;

    public PromiseService(LifeProfile profile) {
        this.profile = profile;
    }

    public void addPromise(Promise promise) {
        profile.addPromise(promise);
    }

    public List<Promise> getAllPromises() {
        return profile.getPromises();
    }

    public List<Promise> findPromisesByCategory(PromiseCategory category) {
        return profile.findPromisesByCategory(category);
    }

    public List<Promise> findPromisesByStatus(PromiseStatus status) {
        return profile.findPromisesByStatus(status);
    }

    public Promise findPromiseById(int id) {
        return profile.findPromiseById(id);
    }

    public Promise removePromise(int id) {
        return profile.removePromise(id);
    }

    public boolean updatePromise(
                                int id,
                                String newTitle,
                                String newDescription,
                                LocalDateTime newDeadline,
                                PromiseCategory newCategory) {
        return profile.updatePromise(id, newTitle, newDescription, newDeadline, newCategory);
    }

    public boolean startPromise(int id) {
        Promise promise = findPromiseById(id);
        if (promise != null) {
            return promise.start();
        }
        return false;
    }

    public boolean completePromise(int id) {
        Promise promise = findPromiseById(id);
        if (promise != null) {
            return promise.complete();
        }
        return false;
    }

    public boolean cancelPromise(int id) {
        Promise promise = findPromiseById(id);
        if (promise != null) {
            return promise.cancel();
        }
        return false;
    }

    public void chescOverduePromise() {

        // Profil ichidagi barcha va'dalarni aylanib chiqamiz.
        for (Promise promise : profile.getPromises()) {

            try {
                // Har bir va'daning muddatini tekshiramiz.
                promise.checkOverdue();

            } catch (RuntimeException exception) {

                // Bitta va'dada xato bo'lsa, qolganlarini
                // tekshirishni davom ettiramiz.
                System.err.println(
                        "Va'da ID " + promise.getId()
                                + " tekshirilayotganda xatolik: "
                                + exception.getMessage()
                );
            }
        }
    }

    public synchronized void startOverdueChecker() {

        // Scheduler to'xtatilgan bo'lsa, uni qayta ishga tushirib bo'lmaydi.
        if (scheduler.isShutdown()) {
            throw new IllegalStateException(
                    "Scheduler to'xtatilgan, qayta ishga tushirib bo'lmaydi."
            );
        }

        // Scheduler allaqachon ishga tushgan bo'lsa, takroran boshlamaymiz.
        if (overdueCheckerStarted) {
            throw new IllegalStateException(
                    "Overdue checker allaqachon ishga tushirilgan."
            );
        }

        // Scheduler ishga tushirilganini belgilaymiz.
        overdueCheckerStarted = true;

        try {
            // Har 1 soniyada va'dalarning muddatini tekshiramiz.
            scheduler.scheduleAtFixedRate(
                    this::chescOverduePromise,
                    0,
                    1,
                    TimeUnit.SECONDS
            );
        } catch (RuntimeException exception) {
            // Ishga tushirish muvaffaqiyatsiz bo'lsa, belgini tiklaymiz.
            overdueCheckerStarted = false;

            // Xatoni yuqoriga uzatamiz.
            throw exception;
        }
    }

    public synchronized void stopOverdueChecker() {

        // Scheduler umuman ishga tushirilmagan bo'lsa,
        // uni to'xtatishga ruxsat bermaymiz.
        if (!overdueCheckerStarted) {
            throw new IllegalStateException(
                    "Overdue checker hali ishga tushirilmagan."
            );
        }

        // Scheduler allaqachon to'xtatilgan bo'lsa,
        // ikkinchi marta to'xtatishga ruxsat bermaymiz.
        if (scheduler.isShutdown()) {
            throw new IllegalStateException(
                    "Overdue checker allaqachon to'xtatilgan."
            );
        }

        // Scheduler'ni to'xtatamiz.
        scheduler.shutdown();
    }

    public int countPromisesByStatus(PromiseStatus status) {
        int count = 0;
        for(Promise promise : profile.getPromises()) {
            promise.checkOverdue();
            if(promise.getStatus() == status) {
                count++;
            }
        }
        return count;
   }

    public List<Promise> getUpcomingPromise() {
        LocalDateTime now = Promise.getCurrentTime();
        LocalDateTime next24Hours = now.plusHours(24);
        List<Promise> upcomingPromises = new ArrayList<>();
        for(Promise promise : profile.getPromises()) {
            promise.checkOverdue();
            if ((promise.getStatus() == PromiseStatus.PENDING
                    || promise.getStatus() == PromiseStatus.IN_PROGRESS)
                    && !promise.getDeadline().isBefore(now)
                    && !promise.getDeadline().isAfter(next24Hours))
                upcomingPromises.add(promise);
        }
        // Va'dalarni eng yaqin muddatdan eng uzoq muddatga saralaymiz.
        upcomingPromises.sort(
                Comparator.comparing(Promise::getDeadline)
        );
        return upcomingPromises;
   }

    public List<Promise> getUpcomingPromises() {

        // Promise klassi ishlatayotgan bir xil vaqt manbasidan foydalanamiz.
        LocalDateTime now = Promise.getCurrentTime();

        // Keyingi 24 soat chegarasi.
        LocalDateTime next24Hours = now.plusHours(24);

        // Natijalarni saqlaydigan yangi ro'yxat.
        List<Promise> upcomingPromises = new ArrayList<>();

        // Profil ichidagi barcha va'dalarni tekshiramiz.
        for (Promise promise : profile.getPromises()) {

            // Muddati o'tgan holatni yangilaymiz.
            promise.checkOverdue();

            // Faqat faol va'dalarni ko'rib chiqamiz.
            if (promise.getStatus() != PromiseStatus.PENDING
                    && promise.getStatus() != PromiseStatus.IN_PROGRESS) {
                continue;
            }

            // Va'daning muddatini olamiz.
            LocalDateTime deadline = promise.getDeadline();

            // Muddati hozirdan boshlab 24 soat ichida bo'lsa, qo'shamiz.
            if (!deadline.isBefore(now)
                    && !deadline.isAfter(next24Hours)) {
                upcomingPromises.add(promise);
            }
        }

        // Eng yaqin muddatli va'dani birinchi o'ringa olib chiqamiz.
        upcomingPromises.sort(
                Comparator.comparing(Promise::getDeadline)
        );

        // Saralangan natijani qaytaramiz.
        return upcomingPromises;
    }

    // Muddati o'tgan va'dalarni qaytaradi.
    public List<Promise> getOverduePromises() {

        // Barcha va'dalarning muddatini tekshirib, statusini yangilaymiz.
        chescOverduePromise();

        // Muddati o'tgan va'dalarni saqlash uchun ro'yxat.
        List<Promise> overduePromises = new ArrayList<>();

        // Profil ichidagi barcha va'dalarni tekshiramiz.
        for (Promise promise : getAllPromises()) {

            // Faqat OVERDUE holatidagi va'dalarni tanlaymiz.
            if (promise.getStatus() == PromiseStatus.OVERDUE) {
                overduePromises.add(promise);
            }
        }

        // Natijani qaytaramiz.
        return overduePromises;
    }

    public boolean isOverdueCheckerShutdown() {
        return scheduler.isShutdown();
    }

}

