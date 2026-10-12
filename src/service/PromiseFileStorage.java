
package service;

import model.Promise;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Base64;
import model.PromiseCategory;
import model.PromiseStatus;
import model.Priority;

import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PromiseFileStorage {

    // Vazifalar saqlanadigan fayl manzili.
    private final Path filePath = Paths.get("promises.txt");

    // Vazifalarni faylga saqlash metodi.

    public void savePromises(List<Promise> promises) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            for (Promise promise : promises) {
                String line =
                        promise.getId() + "\t" +
                                encode(promise.getTitle()) + "\t" +
                                encode(promise.getDescription()) + "\t" +
                                promise.getDeadline() + "\t" +
                                promise.getStatus().name() + "\t" +
                                promise.getCategory().name() + "\t" +
                                promise.getPriority().name();

                writer.write(line);
                writer.newLine();
            }

            System.out.println("Vazifalar faylga saqlandi.");

        } catch (IOException e) {
            System.out.println("Faylga yozishda xatolik: " + e.getMessage());
        }
    }

    private String encode(String value) {
        return Base64.getEncoder().encodeToString(
                value.getBytes(StandardCharsets.UTF_8)
        );
    }


    public List<Promise> loadPromises() {
        // Natijada yuklangan vazifalar shu ro'yxatda bo'ladi.
        List<Promise> promises = new ArrayList<>();

        // Fayl mavjud bo'lmasa, bo'sh ro'yxat qaytaramiz.
        if (!Files.exists(filePath)) {
            return promises;
        }

        // Faylni UTF-8 formatida o'qishni boshlaymiz.
        try (BufferedReader reader = Files.newBufferedReader(
                filePath, StandardCharsets.UTF_8)) {

            String line;

            // Fayldagi har bir qatorni navbat bilan o'qiymiz.
            while ((line = reader.readLine()) != null) {
                // Bo'sh qatorlarni o'tkazib yuboramiz.
                if (line.isBlank()) {
                    continue;
                }

                try {
                    // Tab belgisi bo'yicha maydonlarga ajratamiz.
                    String[] fields = line.split("\t", -1);

                    // Har bir qatorda aynan 7 ta maydon bo'lishi kerak.
                    if (fields.length != 7) {
                        System.out.println("Noto'g'ri qator o'tkazib yuborildi.");
                        continue;
                    }

                    // Matnli qiymatlarni kerakli turlarga aylantiramiz.
                    int id = Integer.parseInt(fields[0]);
                    String title = decode(fields[1]);
                    String description = decode(fields[2]);
                    LocalDateTime deadline =
                            LocalDateTime.parse(fields[3]);
                    PromiseStatus status =
                            PromiseStatus.valueOf(fields[4]);
                    PromiseCategory category =
                            PromiseCategory.valueOf(fields[5]);
                    Priority priority =
                            Priority.valueOf(fields[6]);

                    // Maxsus konstruktor orqali vazifani tiklaymiz.
                    Promise promise = new Promise(
                            id,
                            title,
                            description,
                            deadline,
                            status,
                            category,
                            priority
                    );

                    // Tiklangan vazifani ro'yxatga qo'shamiz.
                    promises.add(promise);

                } catch (IllegalArgumentException e) {
                    // Bitta qator xato bo'lsa, boshqalarini ham o'qishda davom etamiz.
                    System.out.println(
                            "Vazifa qatori o'qilmadi: " + e.getMessage()
                    );
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Faylni o'qishda xatolik: " + e.getMessage()
            );
        }

        // Yuklangan barcha vazifalarni qaytaramiz.
        return promises;
    }

    // Base64 matnini asl matnga qaytaradi.
    private String decode(String value) {
        return new String(
                Base64.getDecoder().decode(value),
                StandardCharsets.UTF_8
        );
    }


}
