package com.tr.hsyn.telefonrehberi.main.code.comment;


import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import io.paperdb.Paper;


/**
 * Yorumlar bu ruh hallerine göre yönlendirilecek
 */
public enum Moody {
    /**
     * Varsayılan ruh hali
     */
    DEFAULT,
    /**
     * Mutlu ruh hali (örnek amaçlı)
     */
    HAPPY;

    private static final String KEY_MOOD = "mood";
    private static final String BOOK     = "book_mood";
    private static final int    _DEFAULT = 0;
    private static final int    _HAPPY   = 1;

    /**
     * Genel ruh hali döndürür.
     *
     * @return mood
     */
    @SuppressWarnings("ConstantConditions")
    public static Moody getMood() {

        int mood = Paper.book(BOOK).read(KEY_MOOD, DEFAULT.ordinal());

        switch (mood) {

            case _DEFAULT:
                xlog.d("Mood DEFAULT");
                return DEFAULT;

            case _HAPPY:
                return HAPPY;
        }

        xlog.d("Wrong moody saved : %d", mood);

        return DEFAULT;
    }

    /**
     * Arama türüne göre
     *
     * @param callType arama türü
     * @return mood
     */
    @SuppressWarnings("ConstantConditions")
    public static int getMood(int callType) {

        return Paper.book(BOOK).read(String.valueOf(callType), DEFAULT.ordinal());
    }

    /**
     * Genel ruh halini kaydet
     *
     * @param mood mood
     */
    public static void saveMood(@NotNull Moody mood) {

        Paper.book(BOOK).write(KEY_MOOD, mood.ordinal());
    }

    /**
     * Arama türü için kaydet.
     *
     * @param mood     mood
     * @param callType arama türü
     */
    public static void saveMood(@NotNull Moody mood, int callType) {

        Paper.book(BOOK).write(String.valueOf(callType), mood.ordinal());
    }
}
