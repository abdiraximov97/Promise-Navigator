package model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class LifeProfile {
    private String name;
    private List<Promise> promises = new ArrayList<>();

    public LifeProfile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

public void addPromise(Promise promise) {
    if(promise == null) {
        throw new IllegalArgumentException("Vazifa 'null' bo'lishi mumkin emas.");
    }
    promises.add(promise);
}

    public List<Promise> getPromises() {
        return Collections.unmodifiableList(promises);
    }

    public Promise findPromiseById(int id) {
        for(Promise promise : promises) {
            if(promise.getId() == id) {
                return promise;
            }
        }
        return null;
    }

    public Promise removePromise(int id) {
        Promise promise = findPromiseById(id);
        if(promise != null) {
            promises.remove(promise);
            return promise;
        }
        return null;
    }

    public boolean updatePromise(int id, String newTitle, String newDescription, LocalDateTime newDeadline) {
        Promise promise = findPromiseById(id);
        if(promise != null) {
//            Yangi nom bo'sh bo'lmasa o'zgartiramiz
            if(newTitle != null && !newTitle.isBlank()) {
                promise.updateTitle(newTitle);
            }
//            Yangi izoh bo'sh bo'lmasa o'zgartiramiz
            if(newDescription != null && !newDescription.isBlank()) {
                promise.updateDescription(newDescription);
            }
//            Dedline berilgan bo'lsa o'zgartiramiz
            if(newDeadline != null) {
                promise.updateDeadline(newDeadline);
            }

            return true;
        }

        return false;
    }
}
