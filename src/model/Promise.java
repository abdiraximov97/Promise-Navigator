
package model;
import java.time.LocalDateTime;
import java.util.Locale;

public class Promise {
    private static int nextId = 1;
    private int id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private PromiseStatus status;
    private PromiseCategory category;

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

        validateDeadline(deadline);

        this.id = nextId++;
        this.title = title.toLowerCase();
        this.description = description.toLowerCase();
        this.deadline = deadline;
        this.status = PromiseStatus.PENDING;
        this.category = category;
    }

//    deadline larni tekshirish
    public void validateDeadline(LocalDateTime deadline) {
        if(deadline == null) {
            throw new IllegalArgumentException("Dedline 'null' bo'lishi mumkin emas.");
        }
        if(deadline.isBefore(LocalDateTime.now()))  {
            throw new IllegalArgumentException("Dedline o'tgan vaqt bo'lishi mumkin emas. " +
                    "\nDeadline vaqti hozirgi vaqtdan kiyin bo'lishi kerak");
        }
    }

    public boolean updateDeadline(LocalDateTime newdeadline) {
        validateDeadline(newdeadline);
        this.deadline = newdeadline;
        if(status == PromiseStatus.OVERDUE) {
            status = PromiseStatus.PENDING;
        }
        return true;
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
//        Vazifani muddati o'tib ketgan va vazifa hali bajarilmagan va vazifa bekor qilinmagan (true)
//        unday bo'lmasa (false) qiymat qaytaradi.
        return  status
                == PromiseStatus.PENDING
                || status == PromiseStatus.IN_PROGRESS
                && deadline.isBefore(LocalDateTime.now());
    }

    public void checkOverdue() {
        if(isOverdue()) {
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

    public void updateTitle(String title) {
        if(title == null && title.isBlank()) {
            throw new IllegalArgumentException("Vazifa nomi bo'sh bo'lishi mumkin emas");
        }
        this.title = title;
    }

    public void updateDescription(String description) {
        if(description == null && description.isBlank()) {
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


    // Vadani matnli formatda qaytaradi.
    @Override
    public String toString() {
        return "Promise{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", status=" + status +
                '}';
    }
}


