package model;

public enum PromiseCategory {
    STUDY("O'qish"),      // O'qish
    WORK("Ish"),       // Ish
    HEALTH("Sog'liq"),     // Sog'liq
    PERSONAL("Shaxsiy"),   // Shaxsiy
    OTHER("Boshqa");     // Boshqa

    private final String displayName;
    PromiseCategory(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}