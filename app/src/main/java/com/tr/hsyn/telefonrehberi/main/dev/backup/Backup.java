package com.tr.hsyn.telefonrehberi.main.dev.backup;


public interface Backup<T> {

    String getName();

    long getDate();

    int getSize();
}