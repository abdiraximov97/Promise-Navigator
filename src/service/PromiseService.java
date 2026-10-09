
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


public class PromiseService {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
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
    public void chescOverduePromise(){
        for(Promise promise : profile.getPromises()) {
            promise.checkOverdue();
        }
    }
    public void startOverdueChecker() {
        scheduler.scheduleAtFixedRate(() -> {
            chescOverduePromise();
        }, 0, 1, TimeUnit.SECONDS);
   }
    public void stopOverdueChecker() {
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
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24Hours = now.plusHours(24);
        List<Promise> upcoming = new ArrayList<>();
        for(Promise promise : profile.getPromises()) {
            promise.checkOverdue();
            if ((promise.getStatus() == PromiseStatus.PENDING
                    || promise.getStatus() == PromiseStatus.IN_PROGRESS)
                    && !promise.getDeadline().isBefore(now)
                    && !promise.getDeadline().isAfter(next24Hours))
               upcoming.add(promise);
        }
        return upcoming;
   }
}

