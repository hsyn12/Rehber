package com.tr.hsyn.telefonrehberi.main.code.database.call;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.db.DBBase;
import com.tr.hsyn.db.cast.DB;
import com.tr.hsyn.db.cast.DBColumn;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Arama kayıtları veri tabanı
 */
public class CallDatabase extends DBBase<Call> implements DBCalls {

    //==============================================================
    //==============================================================
    //======================== Columns ==============================
    private int     nameCol;
    private int     numberCol;
    private int     dateCol;
    private int     typeCol;
    private int     durationCol;
    private int     contactIdCol;
    private int     ringingCol;
    private int     deletedDateCol;
    private int     noteCol;
    private int     extraCol;
    //==============================================================
    /**
     * Bilgilere daha hızlı erişim için kolonlar ilk erişimde ve sadece bir kez tanımlanmaktadır.
     * Kolon bilgileri set edilmişse bu değişken {@code false} durumundadır.
     */
    private boolean isColumnsNotSet = true;
    //==============================================================
    //==============================================================


    public CallDatabase(@NonNull Context context) {

        super(context, new DBInterface());
    }

    @NonNull
    @Override
    protected Call createObject(@NonNull Cursor cursor) {

        if (isColumnsNotSet) {

            nameCol        = cursor.getColumnIndex(NAME);
            numberCol      = cursor.getColumnIndex(NUMBER);
            dateCol        = cursor.getColumnIndex(DATE);
            typeCol        = cursor.getColumnIndex(TYPE);
            durationCol    = cursor.getColumnIndex(DURATION);
            contactIdCol   = cursor.getColumnIndex(CONTACT_ID);
            ringingCol     = cursor.getColumnIndex(RINGING_DURATION);
            deletedDateCol = cursor.getColumnIndex(DELETED_DATE);
            noteCol        = cursor.getColumnIndex(NOTE);
            extraCol       = cursor.getColumnIndex(EXTRA);

            isColumnsNotSet = false;
        }

        String name            = cursor.getString(nameCol);
        String number          = cursor.getString(numberCol);
        long   date            = cursor.getLong(dateCol);
        int    type            = cursor.getInt(typeCol);
        int    duration        = cursor.getInt(durationCol);
        long   contactId       = cursor.getLong(contactIdCol);
        long   ringingDuration = cursor.getLong(ringingCol);
        long   deletedDate     = cursor.getLong(deletedDateCol);
        String note            = cursor.getString(noteCol);
        String extra           = cursor.getString(extraCol);

        return Call.newCall(
                name, number, type, date, duration, extra,
                contactId, note, deletedDate, ringingDuration
        );

    }

    @Override
    @NonNull
    public ContentValues contentValuesOf(@NonNull final Call call) {

        ContentValues values = new ContentValues();

        if (call.getName() != null) values.put(NAME, call.getName());
        else values.putNull(NAME);

        values.put(NUMBER, call.getNumber());
        values.put(DATE, call.getTime());
        values.put(TYPE, call.getType());
        values.put(DURATION, call.getDuration());
        values.put(CONTACT_ID, call.getContactId());
        values.put(DELETED_DATE, call.getDeletedDate());
        values.put(RINGING_DURATION, call.getRingingDuration());
        //values.put(TRACK_TYPE, call.getTrackType());
        values.put(EXTRA, call.getExtra());

        if (call.getNote() != null) values.put(NOTE, call.getNote());
        else values.putNull(NOTE);

        return values;
    }

    @Override
    public boolean delete(@NonNull Call item) {

        return deleteByPrimaryKey(String.valueOf(item.getTime()));
    }


    @Override
    public int delete(@NonNull List<? extends Call> items) {

        List<String> keys = items.stream().map(Call::getTime).map(String::valueOf).collect(Collectors.toList());

        return delete(DBBase.createSelection(DATE, keys));
    }

    @Override
    public int update(@NonNull List<? extends Call> items) {

        return (int) items.stream().filter(this::update).count();
    }

    @Override
    public boolean update(@NonNull Call call) {

        return update(call, call.getTime());
    }

    /**
     * Arama kayıtları bilgileri için veri tabanında oluşturulacak kolonlarını ve bazı temel bilgileri tanımlar.
     */
    private static final class DBInterface implements DB {

        // ==============================================================
        public static final  String TABLE    = "calls";
        private static final String DATABASE = "call_log_calls";

        @NonNull
        @Override
        public String getTableName() {

            return TABLE;
        }

        @NonNull
        @Override
        public String getDatabaseName() {

            return DATABASE;
        }

        @NonNull
        @Override
        public DBColumn[] getColumns() {

            return new DBColumn[]{

                    Text(NAME),
                    Text(NUMBER),
                    Number(DATE).primaryKey(),
                    Number(TYPE),
                    Number(DURATION),
                    Text(CONTACT_ID),
                    Number(DELETED_DATE).defaultValue(0),
                    Number(RINGING_DURATION).defaultValue(0),
                    Text(NOTE),
                    Text(EXTRA)
            };
        }

        @Override
        public String getPrimaryKey() {

            return DATE;
        }
    }
}
