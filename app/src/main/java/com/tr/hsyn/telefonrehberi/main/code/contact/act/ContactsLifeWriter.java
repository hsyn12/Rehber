package com.tr.hsyn.telefonrehberi.main.code.contact.act;


import androidx.annotation.NonNull;

import com.tr.hsyn.life.Life;
import com.tr.hsyn.life.LifeRecorder;
import com.tr.hsyn.life.LifeWriter;


public class ContactsLifeWriter implements LifeWriter {

    private final LifeRecorder lifeDatabase;
    private final Life         live = Life.newLife("Contacts");

    public ContactsLifeWriter(@NonNull LifeRecorder lifeDatabase) {

        this.lifeDatabase = lifeDatabase;
    }

    @NonNull
    @Override
    public LifeRecorder getLifeRecorder() {

        return lifeDatabase;
    }

    @NonNull
    @Override
    public Life getLife() {

        return live;
    }
}
