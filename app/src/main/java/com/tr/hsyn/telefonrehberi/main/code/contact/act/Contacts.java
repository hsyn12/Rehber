package com.tr.hsyn.telefonrehberi.main.code.contact.act;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.tr.hsyn.content.Contents;
import com.tr.hsyn.label.Label;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Contact;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.ContactData;
import com.tr.hsyn.telefonrehberi.main.dev.contact.system.ContactColumns;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Sistem rehberi üzerinde çalışan genel işlemleri tanımlar.<br>
 * <img src="doc-files/contact.png" />
 */
public interface Contacts extends ContactColumns {


    @NotNull
    static Contact newContact(Contact contact) {

        return new BContact(contact);
    }

    @NotNull
    static Contact newContact(long contactId, String name, String pic) {

        return new BContact(contactId, name, pic);
    }

    @NotNull
    static Contact newContact(long contactId, String name, String pic, String bigPic) {

        return new BContact(contactId, name, pic, bigPic);
    }

    @NotNull
    static Contact newContact(long contactId, String name, String pic, String bigPic, List<String> numbers, List<String> emails, String note, List<ContactData> events, List<String> groups, int lookCount, Dates dates, Set<Label> labels) {

        return new BContact(contactId, name, pic, bigPic, numbers, emails, note, events, groups, lookCount, dates, labels);
    }

    /**
     * Kişi listesini verir.<br>
     * Bu çağrıdan önce rehber okuma izni alınmış olmalı.<br>
     * Bu metot kişileri almak için kullanılan ana metot.
     *
     * @param resolver ContentResolver
     * @return Kişi listesi
     */
    @NotNull
    static List<Contact> getContacts(@NotNull final ContentResolver resolver) {

        //region var cursor = resolver.query(...)
        var cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null
        );
        //endregion

        if (cursor != null) {

            // region Setup indexes of contact column
            var contactIdCol = cursor.getColumnIndex(PROJECTION[0]);
            var nameCol      = cursor.getColumnIndex(PROJECTION[1]);
            var picCol       = cursor.getColumnIndex(PROJECTION[2]);
            var bigPicCol    = cursor.getColumnIndex(PROJECTION[3]);
            // endregion

            List<Contact> contacts = new ArrayList<>(cursor.getCount());

            // region Taking data in while loop and creating contact object
            while (cursor.moveToNext()) {

                var contact = newContact(cursor.getLong(contactIdCol),
                        cursor.getString(nameCol),
                        cursor.getString(picCol),
                        cursor.getString(bigPicCol));

                setContact(resolver, contact);
                contacts.add(contact);
            }
            // endregion

            cursor.close();
            return contacts;
        }

        //- return me
        return new ArrayList<>(0);
    }

    /**
     * Creates a new contact intent.
     *
     * @return the new contact intent
     */
    @NonNull
    static Intent createNewContactIntent() {

        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra("finishActivityOnSaveCompleted", true);

        return intent;
    }

    /**
     * Creates an Intent to launch the Android Contacts app with a new contact form.
     * Takes in a phone number as a parameter and adds it to the Intent as an extra, so that it will be pre-filled in the contact form when the user launches it.
     *
     * @param number Phone number
     * @return New {@code Intent}
     */
    @NonNull
    static Intent createNewContactIntent(@NotNull String number) {

        Intent intent = createNewContactIntent();
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
        return intent;
    }

    /**
     * Creates an Intent to add a new contact to the system contact book.
     *
     * @param name   The name of the new contact
     * @param number The phone number of the new contact.
     * @return An Intent to add a new contact to the system contact book.
     */
    @NonNull
    static Intent createNewContactIntent(@NotNull String name, @NotNull String number) {

        Intent intent = createNewContactIntent();
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
        return intent;
    }

    static void setContact(@NotNull final ContentResolver contentResolver, @NotNull Contact contact) {

        var uri    = Contents.getContactEntityUri(contact.getContactId());
        var cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor == null) {return;}

        if (!cursor.moveToFirst()) {

            cursor.close();
            return;
        }

        setContactInfo(cursor, contact);
    }

    /**
     * Sets the contact information for the given contact.
     *
     * @param cursor  The cursor to read the contact information from.
     * @param contact The contact to set the information for.
     */
    @SuppressLint("Range")
    private static void setContactInfo(@NotNull Cursor cursor, @NotNull Contact contact) {

        int               data1Col    = cursor.getColumnIndex(DATA_COLUMNS[0]);
        int               mimeTypeCol = cursor.getColumnIndex(DATA_COLUMNS[1]);
        List<String>      emails      = new ArrayList<>(2);
        List<String>      numbers     = new ArrayList<>(2);
        List<String>      groups      = new ArrayList<>(2);
        List<ContactData> events      = new ArrayList<>(2);

        do {

            var data1 = cursor.getString(data1Col);

            if (data1 == null) continue;

            var mimeType = cursor.getString(mimeTypeCol);
            //var data2    = cursor.getString(data2Col);

            switch (mimeType) {

                case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                    emails.add(data1);
                    break;
                case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
                    addNumbers(cursor, data1Col, numbers);
                    break;
                case ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE:
                    groups.add(data1);
                    break;
                case ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE:
                    addEvents(cursor, data1, events);
                    break;
                case ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE:
                    contact.setNote(data1);
                    break;
            }

        }
        while ((cursor.moveToNext()));

        cursor.close();

        if (!numbers.isEmpty()) contact.setNumbers(numbers);
        if (!emails.isEmpty()) contact.setEmails(emails);
        if (!groups.isEmpty()) contact.setGroups(groups);
        if (!events.isEmpty()) contact.setEvents(events);
    }

    private static void addNumbers(@NotNull Cursor cursor, int data1Col, @NotNull List<String> numbers) {

        final int size   = 13;
        var       number = PhoneNumbers.formatNumber(cursor.getString(data1Col), size);

        var notExist = numbers.stream()
                .noneMatch(nu -> PhoneNumbers.equals(number, nu));

        if (notExist) numbers.add(number);
    }

    @SuppressLint("Range")
    private static void addEvents(@NotNull Cursor cursor, String data1, @NotNull List<? super ContactData> events) {

        int type = cursor.getInt(cursor.getColumnIndex(DATA_COLUMNS[2]));

        events.add(ContactData.newData(data1, type));
    }

    /**
     * Verilen contact id değerine ait kişi için belirli bir mimetype ile kaydedilmiş bilgileri toplar.<br>
     * Bununla bir kişinin telefon numaraları veya email adresleri alınabilir.<br>
     *
     * <pre>{@code
     * List<String> getMailAddresses(@NotNull ContentResolver contentResolver, long contactId) {
     *
     *    return getByMimeType(contentResolver, contactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
     * }
     * }</pre>
     *
     * @param contentResolver contentResolver
     * @param contactId       contactId
     * @param mimeType        mimeType
     * @return list of data
     */
    @NotNull
    static List<String> getByMimeType(@NotNull final ContentResolver contentResolver, final long contactId, @NotNull final String mimeType) {

        var uri    = Contents.getContactEntityUri(contactId);
        var cursor = contentResolver.query(uri, DATA_COLUMNS, DATA_COLUMNS[1] + "=?", new String[]{mimeType}, null);

        if (cursor != null) {

            int          data1Col    = cursor.getColumnIndex(DATA_COLUMNS[0]);
            int          mimeTypeCol = cursor.getColumnIndex(DATA_COLUMNS[1]);
            List<String> datas       = new ArrayList<>(cursor.getCount());

            while (cursor.moveToNext()) {

                var _mimeType = cursor.getString(mimeTypeCol);

                if (mimeType.equals(_mimeType)) {

                    var data1 = cursor.getString(data1Col);

                    if (data1 != null) datas.add(data1);
                }
            }

            cursor.close();
            return datas;
        }

        return new ArrayList<>(0);
    }


}