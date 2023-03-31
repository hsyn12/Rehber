package com.tr.hsyn.selector;


public class EmptyListException extends IllegalArgumentException {

    public EmptyListException() {

        super("Seçim listesi boş olamamalı");
    }
}
