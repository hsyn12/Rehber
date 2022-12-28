package com.tr.hsyn.telefonrehberi.main.code.contact.cast;

import androidx.annotation.Nullable;

import java.util.List;


public interface ContactEvent {

    @Nullable List<ContactData> getEvents();

    void setEvents(@Nullable List<ContactData> events);
}
