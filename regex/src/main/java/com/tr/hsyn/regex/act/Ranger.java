package com.tr.hsyn.regex.act;


import com.tr.hsyn.regex.cast.Range;

import org.jetbrains.annotations.NotNull;


public class Ranger implements Range {

    private final StringBuilder regex = new StringBuilder();

    public Ranger() {}

    public Ranger(char first, char last) {

        regex.append(first).append("-").append(last);
    }

    public Ranger(int first, int last) {

        regex.append(first).append("-").append(last);
    }

    public Ranger(CharSequence sequence) {

        add(sequence);
    }

    @Override
    public @NotNull String getRegex() {

        return "[" + regex + "]";
    }

    @Override
    public @NotNull Range add(@NotNull CharSequence sequence) {

        regex.append(sequence);
        return this;
    }

    @Override
    public @NotNull Range negate() {

        regex.insert(0, "^");
        return this;
    }


}
