package com.tr.hsyn.telefonrehberi.main.dev.contact.system;


import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.Contacts;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public interface SystemContactNumbers {

    @NotNull
    static List<String> getPhoneNumbers(@NotNull final ContentResolver contentResolver, final long contactId) {

        var          numbers  = Contacts.getByMimeType(contentResolver, contactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        List<String> _numbers = new ArrayList<>(numbers.size());

        numbers.forEach(n -> {

            final int size   = 13;
            var       number = PhoneNumbers.formatNumber(n, size);

            var notExist = _numbers.stream()
                    .noneMatch(nu -> PhoneNumbers.equals(number, nu));

            if (notExist) _numbers.add(number);
        });

        return _numbers;
    }

    /**
     * Kişinin {@link ContactsContract.RawContacts} içindeki {@link ContactsContract.RawContacts#_ID}
     * değeri ile bağlı olan telefon numarasını döndürür.
     *
     * @param contentResolver ContentResolver
     * @param rawId           {@link ContactsContract.RawContacts#_ID}
     * @return Telefon numarası, yoksa {@code null}
     */
    @org.jetbrains.annotations.Nullable
    static String getNumber(@NotNull ContentResolver contentResolver, @NotNull String rawId) {

        final Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID + "=?",
                new String[]{rawId},
                null
        );

        if (cursor != null) {

            String number = null;

            final int numberCol = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            if (cursor.moveToFirst()) {

                final int size = 13;
                number = PhoneNumbers.formatNumber(cursor.getString(numberCol), size);
            }

            cursor.close();
            return number;
        }

        return null;
    }

    /**
     * Verilen id ile kayıtlı kişinin telefon numaralarını döndürür.
     *
     * @param contentResolver contentResolver
     * @param contactId       contactId
     * @return Telefon numaraları
     */
    @NotNull
    static List<String> getNumbers(@NotNull ContentResolver contentResolver, @NotNull String contactId) {

        final Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                new String[]{contactId},
                null
        );

        if (cursor != null) {

            List<String> numbers = new ArrayList<>(cursor.getCount());

            final int numberCol = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            while (cursor.moveToNext()) {

                final String _number = PhoneNumbers.formatNumber(cursor.getString(numberCol), 11);

                if (!numbers.contains(_number)) numbers.add(_number);
            }

            cursor.close();
            return numbers;

        }

        return new ArrayList<>(0);
    }


    /**
     * Verilen telefon numarasına sahip kişinin contact id değerini al.
     *
     * @param contentResolver contentResolver
     * @param number          Telefon numarası
     * @return Contact id. Yoksa {@code null}
     */
    static long getContactId(final ContentResolver contentResolver, String number) {

        if (contentResolver == null) return 0;

        final Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER},
                null,
                null,
                null
        );

        if (cursor == null || cursor.getCount() == 0) {
            return 0;
        }

        final int numberCol = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        final int idCol     = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        long      id        = 0L;

        while (cursor.moveToNext()) {

            final String _number = cursor.getString(numberCol);

            if (PhoneNumbers.equals(number, _number)) {

                id = cursor.getLong(idCol);
                break;
            }
        }

        cursor.close();
        return id;
    }

}
