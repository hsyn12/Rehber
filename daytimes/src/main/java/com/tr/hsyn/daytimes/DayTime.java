package com.tr.hsyn.daytimes;


import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class DayTime {

    public static final String DEFAULT_DATE_PATTERN = "d MMMM yyyy EEEE";
    public static final String DEFAULT_TIME_PATTERN = "HH:mm";

    @NonNull
    public static String toString(Context context) {

        if (context == null)
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN + " " + DEFAULT_TIME_PATTERN));

        return toString(context, System.currentTimeMillis());
    }

    @NonNull
    public static String toString(Context context, long time) {

        Instant       instant = Instant.ofEpochMilli(time);
        LocalDateTime date    = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        String dayTime = getDayTime(context, date.getHour(), date.getMinute());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN);

        String clock = context.getString(R.string.clock);

        return String.format("%s, %s %s %s", date.format(dateFormatter), clock, dayTime, date.format(timeFormatter));
    }

    @NonNull
    public static String getDayTime(
            @NonNull final Context context,
            @IntRange(from = 0, to = 23) final int hour,
            @IntRange(from = 0, to = 59) final int minute) {

        if (hour >= 5 && hour <= 7) {return context.getString(R.string.cockcrow);}
        if (hour >= 8 && hour <= 10) {return context.getString(R.string.morning);}
        if (hour == 11 && minute <= 40) {return context.getString(R.string.toward_noon);}
        if (hour >= 11 && hour < 13) {return context.getString(R.string.noon);}
        if (hour >= 13 && hour < 17) {return context.getString(R.string.after_noon);}
        if (hour == 17) {return context.getString(R.string.toward_evening);}
        if (hour >= 18 && hour < 22) {return context.getString(R.string.evening);}
        if (hour >= 22 && hour <= 23) {return context.getString(R.string.night);}
        if (hour == 0) {return context.getString(R.string.middle_night);}
        if (hour >= 1 && hour <= 3) {return context.getString(R.string.after_middle_night);}
        if (hour >= 3 && hour < 5) {return context.getString(R.string.toward_morning);}

        return "";
    }


    public interface DayPart {

        int VERY_MORNING = 0, MORNING = 1, TOWARD_NOON = 2, NOON = 3, AFTER_NOON = 4, TOWARD_NIGHT = 5, NIGHT = 6, MIDDLE_NIGHT = 7, AFTER_NIGHT = 8, TOWARD_MORNING = 9;
    }
}
