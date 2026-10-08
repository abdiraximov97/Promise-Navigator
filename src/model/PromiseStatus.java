
package model;

public enum PromiseStatus {
    PENDING,     //  Kutilmoqda (yangi yaratilgan hali boshlanmagan)
    IN_PROGRESS,  //  Jarayonda (bajarilish boshlangan)
    COMPLETED,  //  Bajarildi (muvaffaqiyatli tugallangan)
    OVERDUE,  //  Muddati o'tgan (deadline dan o'tib ketgan)
    CANCELLED  //  Bekor qilingan
}
