
package model;
import java.time.LocalDateTime;
import java.time.Clock;

public class Promise {
    private static int nextId = 1;
    private final int id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private PromiseStatus status;
    // Vazifaning muddati o'tishidan oldingi holatini saqlaydi.
    private PromiseStatus statusBeforeOverdue;
    private PromiseCategory category;
    // Vazifaning ustuvorlik darajasi.
    private Priority priority = Priority.MEDIUM;
    // Vaqt manbasi. Oddiy dasturda haqiqiy vaqt ishlatiladi.
    private static Clock clock = Clock.systemDefaultZone();

    // Constructor funksiya
    public Promise(String title, String description, LocalDateTime deadline, PromiseCategory category) {
        if(title == null || title.isBlank()) {
            throw new IllegalArgumentException("Vazifani nomi bo'sh bo'lishi mumkin emas.");
        }
        if(description == null || description.isBlank()) {
            throw new IllegalArgumentException("Vazifa uchun izoh bo'sh bo'lishi mumkin emas.");
        }
        if (category == null) {
            throw new IllegalArgumentException(
                    "Vazifa kategoriyasi null bo'lishi mumkin emas."
            );
        }


        this.id = nextId++;
        this.title = title.toLowerCase();
        this.description = description.toLowerCase();
        this.deadline = validateDeadline(deadline);
        this.status = PromiseStatus.PENDING;
        this.category = category;


    }

//    deadline larni tekshirish
    private LocalDateTime validateDeadline(LocalDateTime deadline) {
        // Deadline berilmagan bo'lsa, 7 kun keyingi vaqtni qaytaramiz.
        if (deadline == null) {
            return LocalDateTime.now(clock).plusDays(7);
        }

        // Hozirgi vaqtni bir marta olamiz.
        LocalDateTime now = LocalDateTime.now(clock);

        // O'tgan deadline'ni rad etamiz.
        if (deadline.isBefore(now)) {
            throw new IllegalArgumentException(
                    "Deadline hozirgi vaqtdan keyin bo'lishi kerak."
            );
        }

        // Tekshiruvdan o'tgan deadline'ni qaytaramiz.
        return deadline;
    }

    // Vadani "Jarayonda" holatiga o'tqazadi
    public boolean start() {
        if(status != PromiseStatus.PENDING) {
            return false;
        }
        this.status = PromiseStatus.IN_PROGRESS;
        return true;
    }

    // Vadani "Bajarildi" holatiga o'tqazadi
    public boolean complete() {
        if(status != PromiseStatus.IN_PROGRESS) {
            return false;
        }
        this.status = PromiseStatus.COMPLETED;
        return true;
    }

    // vadani "Bekor qilindi" holatiga o'tqazadi
    public boolean cancel(){
        if(status == PromiseStatus.PENDING || status == PromiseStatus.IN_PROGRESS) {
            this.status = PromiseStatus.CANCELLED;
            return true;
        }
        return false;
    }

    // Vadani "bajarilganmi" yoki "yo'qligini" tekshiradi (true yoki false qaytaradi)
    public boolean isCompleted() {
        return  status == PromiseStatus.COMPLETED ;
    }

    // Vadani muddati o'tganmi yoki muddati o'tmaganmi tekshiradi (true yoki false qaytaradi)
    public boolean isOverdue() {
        // Deadline yo'q bo'lsa, vazifa muddati o'tgan deb hisoblanmaydi.
        if (deadline == null) {
            return false;
        }

        // Faqat bajarilmagan va bekor qilinmagan vazifani tekshiramiz.
        return (status == PromiseStatus.PENDING
                || status == PromiseStatus.IN_PROGRESS)
                && deadline.isBefore(LocalDateTime.now(clock));
    }


    public void checkOverdue() {
        // Vazifaning muddati o'tganini tekshiramiz.
        if (isOverdue()) {

            // Hozirgi holatni OVERDUE qilishdan oldin saqlaymiz.
            statusBeforeOverdue = status;

            // Vazifani muddati o'tgan holatiga o'tkazamiz.
            status = PromiseStatus.OVERDUE;
        }
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public PromiseCategory getCategory() {
        return category;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public PromiseStatus getStatus() {
        return status;
    }

    // Vazifaning ustuvorligini qaytaradi.
    public Priority getPriority() {
        return priority;
    }

    // Vazifaning ustuvorligini o'zgartiradi.
    public void setPriority(Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException(
                    "Ustuvorlik null bo'lishi mumkin emas."
            );
        }

        this.priority = priority;
    }

    public void updateTitle(String title) {
        if(title == null || title.isBlank()) {
            throw new IllegalArgumentException("Vazifa nomi bo'sh bo'lishi mumkin emas");
        }
        this.title = title;
    }

    public void updateDescription(String description) {
        if(description == null || description.isBlank()) {
            throw new IllegalArgumentException("Vazifa izohi bo'sh bo'lishi mumkin emas");
        }
        this.description = description;
    }

    public void updateCategory(PromiseCategory category) {
        if(category == null) {
            throw new IllegalArgumentException("Vazifa turi null bo'lishi mumkin emas.");
        }
        this.category = category;
    }

    public void updateDeadline(LocalDateTime newDeadline) {
        // Yangi muddatni tekshiramiz va tekshirilgan qiymatni saqlaymiz.
        this.deadline = validateDeadline(newDeadline);

        // Agar vazifa muddati o'tgan bo'lsa,
        // yangi muddat berilgach uni PENDING holatiga qaytaramiz.

        if (status == PromiseStatus.OVERDUE) {
            // Vazifaning oldingi holatini tiklaymiz.
            if (statusBeforeOverdue != null) {
                status = statusBeforeOverdue;
            } else {
                // Oldingi holat saqlanmagan bo'lsa,
                // standart holat sifatida PENDING ni tanlaymiz.
                status = PromiseStatus.PENDING;
            }

            // Saqlangan holatni tozalaymiz.
            statusBeforeOverdue = null;
        }

    }

    // Testda vaqt manbasini almashtirish uchun.
    public static void setClock(Clock newClock) {
        if (newClock == null) {
            throw new IllegalArgumentException("Clock null bo'lishi mumkin emas.");
        }

        clock = newClock;
    }

    public void validateUpdate(
            String newTitle,
            String newDescription,
            LocalDateTime newDeadline,
            PromiseCategory newCategory
    ) {
        // Nom bo'sh bo'lmasligi kerak
        if (newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException("Nom bo'sh bo'lishi mumkin emas");
        }
        // Izoh bo'sh bo'lmasligi kerak
        if (newDescription == null || newDescription.isBlank()) {
            throw new IllegalArgumentException("Izoh bo'sh bo'lishi mumkin emas");
        }
        // Kategoriya null bo'lmasligi kerak
        if (newCategory == null) {
            throw new IllegalArgumentException("Kategoriya tanlanishi kerak");
        }
        // Muddat mavjud va kelajakda bo'lishi kerak
        if (newDeadline == null || !newDeadline.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Muddat kelajakda bo'lishi kerak");
        }
    }

    // Promise ishlatayotgan Clock asosida hozirgi vaqtni qaytaramiz.
    public static LocalDateTime getCurrentTime() {
        return LocalDateTime.now(clock);
    }

    // Vadani matnli formatda qaytaradi.
    @Override
    public String toString() {
        return "Promise{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", status=" + status +
                ", category=" + category +
                ", priority=" + priority +
                '}';
    }
}


