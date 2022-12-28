package com.tr.hsyn.telefonrehberi.main.code.contact.cast;

import androidx.annotation.Nullable;

import java.util.List;


public interface ContactNumber {

    @Nullable List<String> getNumbers();

    void setNumbers(@Nullable List<String> numbers);
}
