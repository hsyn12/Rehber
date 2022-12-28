package com.tr.hsyn.telefonrehberi.main.dev.content.sql;


import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.content.handler.SQLContentHandler;
import com.tr.hsyn.perfectsort.PerfectSort;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.Contacts;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.Dates;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Contact;
import com.tr.hsyn.telefonrehberi.main.code.database.contact.DBContact;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;


public class SqlContactHandler implements SQLContentHandler<Contact> {

    private int contactIdCol;
    private int nameCol;
    private int numbersCol;
    private int emailsCol;
    private int lookCountCol;
    private int savedDateCol;
    private int updatedDateCol;
    private int deletedDateCol;
    private int lastLookDateCol;

    @Override
    public void onCreateCursor(@NonNull @NotNull Cursor cursor) {

        contactIdCol    = cursor.getColumnIndex(DBContact.CONTACT_ID);
        nameCol         = cursor.getColumnIndex(DBContact.NAME);
        numbersCol      = cursor.getColumnIndex(DBContact.NUMBERS);
        emailsCol       = cursor.getColumnIndex(DBContact.EMAILS);
        lookCountCol    = cursor.getColumnIndex(DBContact.LOOK_COUNT);
        savedDateCol    = cursor.getColumnIndex(DBContact.SAVED_DATE);
        updatedDateCol  = cursor.getColumnIndex(DBContact.UPDATED_DATE);
        deletedDateCol  = cursor.getColumnIndex(DBContact.DELETED_DATE);
        lastLookDateCol = cursor.getColumnIndex(DBContact.LAST_LOOK_DATE);
    }

    @NonNull
    @Override
    public Contact handle(@NonNull Cursor cursor) {

        Dates dates = Dates.newDates(cursor.getLong(savedDateCol));
        dates.setUpdatedDate(cursor.getLong(updatedDateCol));
        dates.setDeletedDate(cursor.getLong(deletedDateCol));
        dates.setLastLookDate(cursor.getLong(lastLookDateCol));

        return Contacts.newContact(
                cursor.getLong(contactIdCol),
                cursor.getString(nameCol),
                null,
                null,
                DBContact.getList(cursor.getString(numbersCol)),
                DBContact.getList(cursor.getString(emailsCol)),
                null,
                null,
                null,
                cursor.getInt(lookCountCol),
                dates,
                null
        );
    }

    @Nullable
    @Override
    public Comparator<Contact> getComparator() {

        return PerfectSort.comparator(Contact::getName);
    }
}
