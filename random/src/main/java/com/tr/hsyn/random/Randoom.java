package com.tr.hsyn.random;


import java.security.SecureRandom;
import java.util.Random;


/**
 * Rastgele sayı veya boolean değer üreten metotlar sağlar.
 */
public class Randoom {

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * @return Rastgele {@code true} yada {@code false}
     */
    public static boolean getBool() {

        return RANDOM.nextBoolean();
    }

    /**
     * Yüzde ile bir şans ver.
     *
     * @param percent 100 yada 100'den büyük olursa kesinlikle {@code true} döner. 0 yada 0'dan küçük olursa kesinlikle {@code false} döner
     * @return Verilen yüzdeye göre bir şans
     */
    public static boolean getBool(int percent) {

        if (percent >= 100) return true;
        if (percent <= 0) return false;

        return getInt(100 / percent) == 0;
    }

    /**
     * Rastgele bir sayı.
     *
     * @param end Bitiş sınırı. (Sayıya dahil değil)
     * @return [0, end) aralığında bir sayı
     */
    public static int getInt(int end) {

        return RANDOM.nextInt(end);
    }

    /**
     * @return Random integer
     */
    public static int getInt() {

        return RANDOM.nextInt();
    }

    /**
     * İki sayı aralığında bir sayı üretir.
     *
     * @param start İlk sayı başlangıç sınırı. Üretilecek sayıya dahildir.
     * @param end   İkinci sayı bitiş sınırı. Üretilecek sayıya dahil değildir.
     * @return İki sayı aralığında rastgele bir sayı
     */
    public static int getInt(int start, int end) {

        return getInt(end - start) + start;
    }

    /**
     * @return Random long
     */
    public static long getLong() {

        return RANDOM.nextLong();
    }

    /**
     * İki sayı aralığında bir sayı üretir.
     *
     * @param start İlk sayı başlangıç sınırı. Üretilecek sayıya dahildir.
     * @param end   İkinci sayı bitiş sınırı. Üretilecek sayıya dahil değildir.
     * @return İki sayı aralığında rastgele bir sayı
     */
    public static long getLong(long start, long end) {

        return getLong(end - start) + start;
    }

    /**
     * Rastgele bir long sayı döndürür.
     *
     * @param end Bitiş sınırı. Sayıya dahil değil
     * @return [0, end) aralığında bir sayı. Sıfır dahil
     */
    public static long getLong(long end) {

        return Math.abs(RANDOM.nextLong()) % end;
    }

}
