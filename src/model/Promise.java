
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

    // Constructor funksiya
    public Promise(String title, String description, LocalDateTime deadline) {
        if(title == null || title.isBlank()) {
            throw new IllegalArgumentException("Vazifani nomi bo'sh bo'lishi mumkin emas.");
        }
        if(description == null || description.isBlank()) {
            throw new IllegalArgumentException("Vazifa uchun izoh bo'sh bo'lishi mumkin emas.");
        }

        validateDeadline(deadline);

        this.id = nextId++;
        this.title = title.toLowerCase();
        this.description = description.toLowerCase();
        this.deadline = deadline;
        this.status = PromiseStatus.PENDING;
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

    // Vadani "Jarayonda" holatiga o'tqazadi
    public boolean start() {
        if(status == PromiseStatus.PENDING) {
            this.status = PromiseStatus.IN_PROGRESS;
            return true;
        }
        return false;
    }

    // Vadani "Bajarildi" holatiga o'tqazadi
    public boolean complete() {
        if(status == PromiseStatus.IN_PROGRESS) {
            this.status = PromiseStatus.COMPLETED;
            return true;
        }
        return false;
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
        return deadline.isBefore(LocalDateTime.now()) && status != PromiseStatus.COMPLETED && status != PromiseStatus.CANCELLED;
    }

    public void checkOverdue() {
        if(status == PromiseStatus.PENDING || status == PromiseStatus.IN_PROGRESS) {
            if(deadline.isBefore(LocalDateTime.now())) {
                status = PromiseStatus.OVERDUE;
            }
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

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public PromiseStatus getStatus() {
        return status;
    }

    public void setDeadline(LocalDateTime deadline) {
        validateDeadline(deadline);
        this.deadline = deadline;
    }

    public void setStatus(PromiseStatus status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
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


