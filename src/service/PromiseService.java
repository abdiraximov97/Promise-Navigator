
package service;
import model.LifeProfile;
import model.Promise;
import java.time.LocalDateTime;
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

    public Promise findPromise(String title) {
        for (Promise promise : profile.getPromises()) {
            if (promise.getTitle().equalsIgnoreCase(title)) {
                    return promise;
            }
        }
        return null;
    }

    public Promise findPromiseById(int id) {
        return profile.findPromiseById(id);
    }

    public Promise removePromise(int id) {
        return profile.removePromise(id);
    }

    public boolean updatePromise(int id, String newTitle, String newDescription, LocalDateTime newDeadline) {
        return profile.updatePromise(id, newTitle, newDescription, newDeadline);
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


   public void startOverdueChecker() {
        scheduler.scheduleAtFixedRate(() -> {
            for (Promise promise : profile.getPromises()) {
                promise.checkOverdue();
            }
        }, 0, 1, TimeUnit.SECONDS);
   }

   public void stopOverdueChecker() {
        scheduler.shutdown();
   }
}

