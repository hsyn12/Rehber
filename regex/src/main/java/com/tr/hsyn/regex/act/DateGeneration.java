package com.tr.hsyn.regex.act;


import com.tr.hsyn.regex.cast.DateGenerator;

import org.jetbrains.annotations.NotNull;


public class DateGeneration implements DateGenerator {

    private String pattern;

    public DateGeneration(@NotNull String pattern) {

        this.pattern = pattern;
    }

    @Override
    public @NotNull DateGenerator twoDigitDay() {

        pattern += "dd";
        return this;
    }

    @Override
    public @NotNull DateGenerator twoDigitMonth() {

        pattern += "MM";
        return this;
    }

    @Override
    public @NotNull DateGenerator oneDigitMonth() {

        pattern += "M";
        return this;
    }

    @Override
    public @NotNull String getPattern() {

        return pattern;
    }

    @Override
    public @NotNull DateGenerator oneDigitDay() {

        pattern += "d";
        return this;
    }

    @Override
    public @NotNull DateGenerator twoDigitYear() {

        pattern += "yy";
        return this;
    }

    @Override
    public @NotNull DateGenerator fourDigitYear() {

        pattern += "yyyy";
        return this;
    }

    @Override
    public @NotNull DateGenerator shortMonthName() {

        pattern += "MMM";
        return this;
    }

    @Override
    public @NotNull DateGenerator longMonthName() {

        pattern += "MMMM";
        return this;
    }

    @Override
    public @NotNull DateGenerator shortDayName() {

        pattern += "EEE";
        return this;
    }

    @Override
    public @NotNull DateGenerator longDayName() {

        pattern += "EEEE";
        return this;
    }

    @Override
    public @NotNull DateGenerator shortTime() {

        pattern += "HH:mm";
        return this;
    }

    @Override
    public @NotNull DateGenerator longTime() {

        pattern += "HH:mm:ss";
        return this;
    }

    @Override
    public @NotNull DateGenerator hour() {

        pattern += "HH";
        return this;
    }

    @Override
    public @NotNull DateGenerator minute() {

        pattern += "mm";
        return this;
    }

    @Override
    public @NotNull DateGenerator second() {

        pattern += "ss";
        return this;
    }

    @Override
    public @NotNull DateGenerator space() {

        pattern += " ";
        return this;
    }

    @Override
    public @NotNull DateGenerator colon() {

        pattern += ":";
        return this;
    }

    @Override
    public @NotNull DateGenerator hyphen() {

        pattern += "-";
        return this;
    }

    @Override
    public @NotNull DateGenerator dot() {

        pattern += ".";
        return this;
    }

    @Override
    public @NotNull DateGenerator slash() {

        pattern += "/";
        return this;
    }

    @Override
    public @NotNull DateGenerator backSlash() {

        pattern += "\\";
        return this;
    }

    @Override
    public @NotNull DateGenerator putSeparator(@NotNull String separator) {

        pattern += separator;
        return this;
    }
}
