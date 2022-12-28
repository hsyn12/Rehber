package com.tr.hsyn.regex.act;


import com.tr.hsyn.regex.cast.Regex;

import org.jetbrains.annotations.NotNull;


@SuppressWarnings("DefaultLocale")
public class Matchy implements Regex {

    /**
     * The regex to match
     */
    private final StringBuilder regex = new StringBuilder();

    public Matchy(@NotNull String regex) {

        add(regex);
    }

    @Override
    public String toString() {

        return "Match{" +
               "regex='" + regex + '\'' +
               '}';
    }

    @NotNull
    @Override
    public String getRegex() {

        return regex.toString();
    }

    @Override
    public @NotNull Regex add(@NotNull CharSequence s) {

        regex.append(s);
        return this;
    }

    @Override
    public @NotNull Regex addFirst(@NotNull CharSequence s) {

        regex.insert(0, s);
        return this;
    }

    @Override
    public @NotNull Regex add(char c) {

        regex.append(c);
        return this;
    }

    @Override
    public @NotNull Regex add(int i) {

        regex.append(i);
        return this;
    }

    @NotNull
    @Override
    public Regex asGroup(String name) {

        var r = regex.toString();

        regex.delete(0, regex.length());
        return add(String.format("(?<%s>%s)", name, r));
    }

}
