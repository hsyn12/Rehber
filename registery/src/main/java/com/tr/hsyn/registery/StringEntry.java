package com.tr.hsyn.registery;


import org.jetbrains.annotations.NotNull;


public record StringEntry(@NotNull String getKey, String getValue) implements Entry<String> {}
