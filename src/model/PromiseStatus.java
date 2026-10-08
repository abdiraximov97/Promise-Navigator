
package model;

public enum PromiseStatus {
    PENDING("Kutilmoqda"),     //  Kutilmoqda (yangi yaratilgan hali boshlanmagan)
    IN_PROGRESS("Jarayonda"),  //  Jarayonda (bajarilish boshlangan)
    COMPLETED("Bajarildi"),  //  Bajarildi (muvaffaqiyatli tugallangan)
    OVERDUE("Muddati o'tgan"),  //  Muddati o'tgan (deadline dan o'tib ketgan)
    CANCELLED("Bekor qilingan");   //  Bekor qilingan

    private final String displayName;
    PromiseStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
