package com.tr.hsyn.regex.cast;


import com.tr.hsyn.regex.Rego;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class WordGeneration implements WordGenerator {

    private final Random                    random             = new Random(System.currentTimeMillis());
    private final List<java.lang.Character> excludedCharacters = new ArrayList<>();
    private final Integer[]                 regexCodes;
    private final int                       length;
    private       String                    characterSet;

    public WordGeneration(@NotNull CharSequence like, char @NotNull ... excludeCharacters) {

        characterSet = null;
        length       = like.length();
        regexCodes   = Rego.Dev.toRegexCodes(like, true, true);
        exclude(excludeCharacters);
    }

    public WordGeneration(@NotNull CharSequence like, String characterSet) {

        regexCodes        = null;
        length            = like.length();
        this.characterSet = characterSet;
    }

    @Override
    public int getNextIndex(int end) {

        return random.nextInt(end);
    }

    @Override
    public boolean shouldExclude(char c) {

        return excludedCharacters.contains(c);
    }

    @Override
    public Integer[] getRegexCodes() {

        return regexCodes;
    }

    @Override
    public int getLength() {

        return length;
    }

    @Override
    public @Nullable String getCharacterSet() {

        return characterSet;
    }

    @Override
    public WordGenerator setCharacterSet(@Nullable String set) {

        this.characterSet = set;
        return this;
    }

    @Override
    public WordGenerator exclude(char @NotNull ... chars) {

        for (var c : chars) excludedCharacters.add(c);
        return this;
    }


}
