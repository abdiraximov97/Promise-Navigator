package util;
import model.Priority;
import model.PromiseCategory;
import model.PromiseStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InputHelper {

    private final Scanner scanner;

    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                int number = scanner.nextInt();
                scanner.nextLine();
                return number;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println(
                        "Xatolik: faqat son kiriting."
                );
            }
        }
    }

    public String readRequiredString(String message) {
        while (true) {
            System.out.print(message);
            String value = scanner.nextLine();
            if(value.isBlank()) {
                System.out.println("Xatolik, qiymat bo'sh bo'lishi mumkin emas");
                continue;
            }
            return value;
        }
    }

    public LocalDateTime readDeadline(String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        while (true) {
            System.out.print(message);
            String dedlineInput = scanner.nextLine();

            if(dedlineInput.isBlank()) {
                System.out.println("Xatolik, deadline bo'sh bo'lmasligi kerak");
                continue;
            }
            try {
                LocalDateTime deadline = LocalDateTime.parse(dedlineInput, formatter);
                if(deadline.isBefore(LocalDateTime.now())) {
                    System.out.println("Xatolik: deadline vaqti o'tgan bo'lishi mumkin emas");
                    continue;
                }
                return deadline;
            } catch (DateTimeParseException e) {
                System.out.println("Xatolik: faqat yyy.MM.dd HH:mm formatida kiriting.");
                System.out.println("Masalan: 2023.10.07 12:30");
            }
        }
    }

    public PromiseCategory readOptionalCategory(String message) {
        while (true) {
            System.out.println(message);
            System.out.println("1. O'qish");
            System.out.println("2. Ish");
            System.out.println("3. Sog'liq");
            System.out.println("4. Shaxsiy");
            System.out.println("5. Boshqa");
            System.out.println("Agar kategoriyani o'zgartirmasangiz [Enter] ni bosing");

            String input = scanner.nextLine();

            if(input.isBlank()) {
                return null;
            }

            switch (input) {

                case "1":
                    return PromiseCategory.STUDY;

                case "2":
                    return PromiseCategory.WORK;

                case "3":
                    return PromiseCategory.HEALTH;

                case "4":
                    return PromiseCategory.PERSONAL;

                case "5":
                    return PromiseCategory.OTHER;

                default:
                    System.out.println(
                            "Xatolik: 1-5 oralig'ida tanlang."
                    );
            }
        }
    }

    public PromiseStatus readStatus(String message) {
        while (true) {
            System.out.println(message);
            System.out.println("1. Kutilmoqda");
            System.out.println("2. Jarayonda");
            System.out.println("3. Bajarildi");
            System.out.println("4. Muddati o'tgan");
            System.out.println("5. Bekor qilingan");

            int choice = readInt("Tanlang: ");
            switch (choice) {
                case 1: return PromiseStatus.PENDING;
                case 2: return PromiseStatus.IN_PROGRESS;
                case 3: return PromiseStatus.COMPLETED;
                case 4: return PromiseStatus.OVERDUE;
                case 5: return PromiseStatus.CANCELLED;
                default:
                    System.out.println("Xatolik, 1-5 orasida son tanlang");
            }
        }
    }

    public String readOptionalString(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }


    public LocalDateTime readOptionalDeadline(String message) {
        while (true) {
            System.out.println(message);
            System.out.print(
                    "Deadline kiriting (yyyy.MM.dd HH:mm) " +
                            "yoki avtomatik 7 kun uchun Enter bosing: "
            );

            String value = scanner.nextLine().trim();

            // Bo'sh qoldirilsa, null qaytaramiz.
            if (value.isEmpty()) {
                return null;
            }

            try {
                return LocalDateTime.parse(
                        value,
                        DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
                );
            } catch (DateTimeParseException e) {
                System.out.println(
                        "Noto'g'ri format. Masalan: 2026.10.17 18:30"
                );
            }
        }
    }


    public PromiseCategory readCategory(String message) {
        while (true) {
            System.out.println(message);
            System.out.println("1. O'qish");
            System.out.println("2. Ish");
            System.out.println("3. Sog'lik");
            System.out.println("4. Shaxsiy");
            System.out.println("5. Boshqa");

            int choice = readInt("Tanlang: ");
            switch (choice) {
                case 1: return PromiseCategory.STUDY;
                case 2: return PromiseCategory.WORK;
                case 3: return PromiseCategory.HEALTH;
                case 4: return PromiseCategory.PERSONAL;
                case 5: return PromiseCategory.OTHER;
                default:
                    System.out.println("Xatolik: 1-5 orasida son tanlang.");
            }
        }
    }

    // Foydalanuvchidan ustuvorlikni tanlashni so'raydi.
    public Priority readPriority(String message) {

        while (true) {
            System.out.println(message);
            System.out.println("1. HIGH   - Yuqori");
            System.out.println("2. MEDIUM - O'rta");
            System.out.println("3. LOW    - Past");

            // Foydalanuvchi tanlovini olamiz.
            int choice = readInt("Tanlovingiz: ");

            // Tanlangan raqamni Priority qiymatiga aylantiramiz.
            switch (choice) {
                case 1:
                    return Priority.HIGH;
                case 2:
                    return Priority.MEDIUM;
                case 3:
                    return Priority.LOW;
                default:
                    System.out.println(
                            "Noto'g'ri tanlov. 1, 2 yoki 3 ni kiriting."
                    );
            }
        }
    }
}