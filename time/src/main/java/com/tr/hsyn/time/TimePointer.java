package com.tr.hsyn.time;


import org.jetbrains.annotations.Range;

import java.time.LocalDateTime;


/**
 * Zaman İşaretçisi. Belirli bir zamanı milisaniye olarak geri döndüren metotlar sunar.
 *
 * @author hsyn 25 Kasım 2022 Cuma 14:07
 */
public interface TimePointer {

    /**
     * Verilen yılın zamanını döndürür.
     * Ay, saat, dakika ve saniye şimdiki zamandan alınır.<br>
     * Ancak gidilen yıla göre gün ismi bugünkü gün isminden farklı olabilir.
     * <br><br>
     *
     * <pre>
     * // Şimdiki zaman
     * System.out.println(Time.toString("d MMMM yyyy EEEE HH:mm")); // 25 Kasım 2022 Cuma 13:49
     * // 1981 yılı
     * System.out.println(Time.toString(Pointer.at(1981), "d MMMM yyyy EEEE HH:mm")); // 25 Kasım 1981 Çarşamba 13:49
     * </pre>
     *
     * @param year İşaret edilmek istenen yıl
     * @return Zamanın milisaniye değeri
     */
    default long at(int year) {

        return LocalDateTime.now().withYear(year).toEpochSecond(Date.DEFAULT_ZONE_OFFSET) * 1000L;
    }

    /**
     * Verilen tarihin milisaniye olarak zamanını döndürür.
     * Saat {@code 00:00:00} olarak ayarlanır.
     *
     * @param day   Gün 1-31
     * @param month Ay 1-12
     * @param year  Yıl
     * @return Zaman değeri
     */
    default long at(@Range(from = 1, to = 31) int day, @Range(from = 1, to = 12) int month, int year) {

        return Time.of(day, month, year).getMillis();
    }

    /**
     * Verilen tarihin milisaniye olarak zamanını döndürür.
     *
     * @param day    Gün 1-31
     * @param month  Ay 1-12
     * @param year   Yıl
     * @param hour   Saat
     * @param minute Dakika
     * @return Zaman değeri
     */
    default long at(
            @Range(from = 1, to = 31) int day,
            @Range(from = 1, to = 12) int month,
            int year,
            @Range(from = 0, to = 23) int hour,
            @Range(from = 0, to = 59) int minute) {

        return Time.of(day, month, year, hour, minute).getMillis();
    }
}
