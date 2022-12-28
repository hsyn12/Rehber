package com.tr.hsyn.regex.act;


import com.tr.hsyn.regex.cast.Alternate;
import com.tr.hsyn.regex.cast.Range;
import com.tr.hsyn.regex.cast.Regex;

import org.jetbrains.annotations.NotNull;


public class Alternative implements Alternate {

    private final StringBuilder regex = new StringBuilder("(");

    public Alternative() {}

    public Alternative(int i) {add(i);}

    public Alternative(char c) {

        add(c);
    }

    public Alternative(char c, char c2) {

        add(c).add(c2);
    }

    public Alternative(int i, int j) {

        add(i).add(j);
    }

    public Alternative(@NotNull Regex regex) {

        add(regex.getRegex());
    }

    public Alternative(@NotNull Alternate alternate) {

        add(alternate.getRegex());
    }

    public Alternative(@NotNull Range range) {

        add(range.getRegex());
    }

    private void deleteLast() {

        regex.setCharAt(regex.length() - 1, ')');
    }

    @Override
    public @NotNull String getRegex() {

        deleteLast();
        return regex.append(')').toString();
    }

    @Override
    public @NotNull Alternate add(@NotNull String s) {

        regex.append(s).append('|');
        return this;
    }

    @Override
    public @NotNull Alternate add(int i) {

        regex.append(i).append('|');
        return this;
    }

    @Override
    public @NotNull Alternate add(char c) {

        regex.append(c).append('|');
        return this;
    }

    @Override
    public String toString() {

        return "Alternate{" +
               "regex='" + getRegex() + "'" +
               '}';
    }
}
