package com.tr.hsyn.telefonrehberi.main.code.contact.cast;

import androidx.annotation.Nullable;

import java.util.List;


public interface ContactMail {

    @Nullable List<String> getEmails();

    void setEmails(@Nullable List<String> emails);

}
