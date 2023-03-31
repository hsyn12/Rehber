package com.tr.hsyn.telefonrehberi.code;


import com.tr.hsyn.random.Randoom;

import java.util.Arrays;
import java.util.List;


/**
 * <h1>WordGenerator</h1>
 * <p>
 * Rastgele kelime üreticisi.
 * Üretilen kelimeler rastgeledir ve anlamlı olması beklenmez.
 *
 * @author hsyn 27 Eylül 2021 Pazartesi 10:50:57
 */
public class WordGenerator {

    private final List<String> wowels       = Arrays.asList("a", "ı", "o", "u", "e", "i", "ö", "ü");
    private final char[]       SOURCE       = "abcçdefghıijklmnoöprsştuüvyz".toCharArray();
    private final char[]       source;
    private final int          MIN_LENGTH   = 6;
    private final int          MAX_LENGTH   = 18;
    private final int          sourceLength = SOURCE.length;
    private final int          minLength;
    private final int          maxLength;
    private final boolean      isBigFirstChar;

    /**
     * Varsayılan değerlerle bir üretici oluşturur.
     * Bu üretici, kelimelerin üretileceği kaynak olarak türk alfabesini kullanılır.
     * Farklı bir kaynak kullanılmak isteniyorsa {@link #WordGenerator(char[])} kurucusu kullanılabilir.
     */
    public WordGenerator() {

        source         = SOURCE;
        minLength      = MIN_LENGTH;
        maxLength      = MAX_LENGTH;
        isBigFirstChar = false;
    }

    /**
     * Yeni bir üretici oluşturur.
     * Bu üretici, üreteceği kelimeler için verilen kaynağı kullanır.
     *
     * @param source Kelimelerin üretileceği kaynak
     */
    public WordGenerator(char[] source) {

        if (source != null) this.source = source;
        else this.source = SOURCE;

        minLength      = MIN_LENGTH;
        maxLength      = MAX_LENGTH;
        isBigFirstChar = false;
    }

    /**
     * Yeni bir üretici oluşturur.
     * Bu üreticinin ürettiği kelimelerin minimum uzunluğu verilen değerde olacaktır.
     *
     * @param minLength Üretilecek kelimelerin minimum uzunluğu
     */
    public WordGenerator(int minLength, int maxLength) {

        source         = SOURCE;
        isBigFirstChar = false;

        if (isNotValidMinLenght(minLength)) {

            System.out.println("Minimum uzunluk :" + MIN_LENGTH + ", bundan daha küçük bir uzunluk belirtilemez : " + minLength);
            this.minLength = MIN_LENGTH;
        }
        else {

            this.minLength = minLength;
        }

        if (isNotValidMaxLenght(maxLength)) {

            System.out.println("Maksimum uzunluk :" + MAX_LENGTH + ", bundan daha büyük bir uzunluk belirtilemez : " + maxLength);
            this.maxLength = MAX_LENGTH;
        }
        else {

            this.maxLength = maxLength;
        }


    }

    /**
     * Yeni bir üretici oluşturur.
     *
     * @param source             Kaynak
     * @param minLength          Minimum kelime uzunluğu
     * @param maxLength          Maksimum kelime uzunluğu
     * @param uppercaseFirstChar İlk har büyük mü olsun?
     */
    public WordGenerator(char[] source, int minLength, int maxLength, boolean uppercaseFirstChar) {

        isBigFirstChar = uppercaseFirstChar;

        if (source != null) this.source = source;
        else this.source = SOURCE;

        if (isNotValidMinLenght(minLength)) {

            System.out.println("Minimum uzunluk :" + MIN_LENGTH + ", bundan daha küçük bir uzunluk belirtilemez : " + minLength);
            this.minLength = MIN_LENGTH;
        }
        else {

            this.minLength = minLength;
        }

        if (isNotValidMaxLenght(maxLength)) {

            System.out.println("Maksimum uzunluk :" + MAX_LENGTH + ", bundan daha büyük bir uzunluk belirtilemez : " + maxLength);
            this.maxLength = MAX_LENGTH;
        }
        else {

            this.maxLength = maxLength;
        }
    }

    /**
     * @param minLength Üretilecek kelimenin minimum uzunluğu
     */
    private boolean isNotValidMinLenght(int minLength) {

        return (minLength >= MIN_LENGTH && (MAX_LENGTH - minLength) >= MIN_LENGTH);
    }

    /**
     * @param maxLength Üretilecek kelimenin maksimum uzunluğu
     */
    private boolean isNotValidMaxLenght(int maxLength) {

        return (maxLength <= MAX_LENGTH && maxLength >= minLength);
    }

    /**
     * @return Rastgele bir kelime
     */
    public String generate() {

        int    length   = Randoom.getInt(minLength, maxLength);
        char[] word     = new char[length];
        char   lastChar = ' ';

        for (int i = 0; i < length; ) {

            char c = source[Randoom.getInt(sourceLength)];

            if (c == lastChar) continue;

            if (wowels.contains(String.valueOf(c))) {

                if (wowels.contains(String.valueOf(lastChar))) {

                    continue;
                }

                word[i++] = c;
            }
            else {

                if (wowels.contains(String.valueOf(lastChar))) {

                    word[i++] = c;
                }
                else continue;
            }

            lastChar = c;
        }

        if (isBigFirstChar) word[0] = Character.toUpperCase(word[0]);
        return new String(word);
    }
}