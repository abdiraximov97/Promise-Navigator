package model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class LifeProfile {
    private final String name;
    private final List<Promise> promises = new ArrayList<>();

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

    public List<Promise> findPromisesByStatus(PromiseStatus status) {
        if(status == null) {
            throw new IllegalArgumentException("Vazifa statusi null bo'lishi mumkin emas.");
        }
        List<Promise> result = new ArrayList<>();
        for(Promise promise : promises) {
            if(promise.getStatus() == status) {
                result.add(promise);
            }
        }
        return result;
    }

    public List<Promise> findPromisesByCategory(PromiseCategory category) {
        if(category == null) {
            throw new IllegalArgumentException("Vazifa kategoriyasi null bo'lishi mumkin emas.");
        }
        List<Promise> result = new ArrayList<>();

        for (Promise promise : promises) {
            if (promise.getCategory() == category) {
                result.add(promise);
            }
        }
        return result;
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

    public boolean updatePromise(int id, String newTitle, String newDescription, LocalDateTime newDeadline, PromiseCategory newCategory) {
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
//            Kategoriya berilgan bo'lsa o'zgartiramiz
            if(newCategory != null) {
                promise.updateCategory(newCategory);
            }

            return true;
        }

        return false;
    }
}
