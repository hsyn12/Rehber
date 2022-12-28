package com.tr.hsyn.telefonrehberi.main.code.contact.cast;

import androidx.annotation.Nullable;

import java.util.List;


public interface ContactAccount {

    @Nullable
    List<Account> getAccounts();

    void setAccounts(@Nullable List<Account> accounts);
}
