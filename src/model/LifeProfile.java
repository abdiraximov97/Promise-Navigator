package model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class LifeProfile {
    private String name;
    private List<Promise> promises = new ArrayList<>();

    public LifeProfile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                promise.setTitle(newTitle);
            }
//            Yangi izoh bo'sh bo'lmasa o'zgartiramiz
            if(newDescription != null && !newDescription.isBlank()) {
                promise.setDescription(newDescription);
            }
//            Dedline berilgan bo'lsa o'zgartiramiz
            if(newDeadline != null) {
                promise.setDeadline(newDeadline);
                if(promise.getStatus() == PromiseStatus.OVERDUE) {
                    promise.setStatus(PromiseStatus.PENDING);
                }
            }

            return true;
        }

        return false;
    }
}
