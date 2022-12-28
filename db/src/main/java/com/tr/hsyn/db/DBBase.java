package com.tr.hsyn.db;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.db.actor.DBOperator;
import com.tr.hsyn.db.cast.DB;
import com.tr.hsyn.db.cast.DBColumn;
import com.tr.hsyn.warnerlabel.Remember;
import com.tr.hsyn.xlog.xlog;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


/**
 * <h1>DBBase</h1>
 * <p>
 * Daha az zahmetle iş yapan bir veri tabanı.
 * Genişlet ve kullan.
 *
 * @param <T> Veri tabanında tutulacak model türü
 * @author hsyn 02 Nisan 2021 Cuma 12:02:25
 */
@Remember(note = "Bu sınıf hala tam olarak olgunlaşmış değil")
public abstract class DBBase<T> extends DBOperator<T> {

    /**
     * Veri tabanı oluştur.<br>
     * Bir veri tabanı için en önemli bilgi kolonlardır. (Elzemdir)<br>
     * Bir kolon, altına yazılacak bilgiler için bir isim niteliğindedir.
     * Ancak sadece isim yeterli değil, kaydedilecek bilginin türü de gerekir. (int, string vs.)<br>
     * Kolonlar ile ilgili tüm bilgiler {@link DB} arayüzü ile alınır.
     * Bu arayüz aynı zamanda tablo oluşturacak sql komutunu da hazırlar.
     * Kullanıcının yapması gerekenler {@link DB} arayüzünde bildirilmektedir.
     *
     * @param context     context
     * @param dbInterface veri tabanı için gerekli bilgiler
     */
    public DBBase(@NonNull Context context, @NonNull DB dbInterface) {

        super(context, dbInterface);
    }

    @NonNull
    private static String createSelection(@NonNull String key) {

        return String.format("%s=?", key);
    }

    /**
     * {@code x in (a,b,c,d)} şeklinde bir seçim sorgusu oluşturur.
     *
     * @param key   Veri tabanındaki kolon ismi
     * @param value kolondaki değerler
     * @return Seçim string'i
     */
    @NonNull
    public static String createSelection(@NonNull String key, @NonNull List<String> value) {

        return String.format("%s in (%s)", key, String.join(",", value));
    }

    @NonNull
    private static String[] createArg(@NonNull String arg) {

        return new String[]{arg};
    }

    /**
     * Verilen nesne için bilgileri oluştur.<br>
     * {@code ContentValues} nesnesi, veri tabanı işlemleri için gerekli bir nesne ve
     * veri tabanının kaydettiği nesnenin {@code ContentValues} nesnesine dönüştürülmesi zorunludur.<br>
     * {@link DBBase} sınıfı soyut ve generic olduğu için kaydedilen nesne hakkında bir bilgisi yoktur.
     * Bu yüzden bu metodu uygulamak alt sınıfların görevidir.
     *
     * @param item Nesne
     * @return Nesne bilgileri
     */
    protected abstract ContentValues contentValuesOf(T item);

    /**
     * Satırları nesneye dönüştür.<br>
     * {@link DBBase} sınıfı soyut ve generic olduğu için kaydedilen nesne hakkında bir bilgisi yoktur.<br>
     * Bu yüzden alt sınıflar kaydettikleri satırları yeniden nesneye dönüştürmek zorundadır.<br>
     * {@code Cursor}, bir satıra konumlanmış ve bilgi alınmaya hazır durumda gelir.
     * Bu nesnenin konumunu değiştirme, konum otomatik olarak ilerlemekte.
     *
     * @param cursor cursor
     * @return Nesne
     */
    @NonNull
    protected abstract T createObject(@NonNull final Cursor cursor);

    @Override
    public boolean add(T item) {

        return add(contentValuesOf(item));
    }

    @Override
    public int add(@NonNull List<? extends T> items) {

        return add(items, this::contentValuesOf);
    }

    @Override
    public int add(@NonNull List<? extends T> items, @NonNull Function<? super T, ContentValues> valuesFunction) {

        SQLiteDatabase db      = null;
        int            success = 0;

        try {

            db = getWritableDatabase();
            db.beginTransaction();

            for (var i : items) {

                var row = db.insert(databaseInterface.getTableName(), null, valuesFunction.apply(i));

                if (row != -1) success++;
            }

            db.setTransactionSuccessful();
        }
        finally {

            if (db != null) {

                db.endTransaction();
            }
        }

        return success;
    }

    @Override
    public boolean update(@NonNull T item, long primaryValue) {

        return update(item, String.valueOf(primaryValue));
    }

    @Override
    public boolean update(@NonNull T item, @NonNull String primaryValue) {

        var key = databaseInterface.getPrimaryKey();

        if (key == null) {

            throw new IllegalArgumentException("Primary key not defined");
        }

        return update(contentValuesOf(item), key + "=?", new String[]{primaryValue});
    }

    @Override
    public boolean update(@NonNull ContentValues values, @NonNull String primaryKey) {

        String primaryColumn = databaseInterface.getPrimaryKey();

        if (primaryColumn != null) {

            return super.update(values, primaryColumn + "=?", new String[]{primaryKey});
        }


        xlog.w("Primary key not found");
        return false;
    }

    @Override
    public int delete(@NonNull String selection) {

        return super.delete(selection, null);
    }

    @Override
    public boolean deleteByPrimaryKey(@NonNull String primaryValue) {

        String key = databaseInterface.getPrimaryKey();

        if (key == null) {

            DBColumn item = Arrays.stream(databaseInterface.getColumns()).filter(DBColumn::isPrimaryKey).findFirst().orElse(null);

            if (item != null) {

                key = item.getName();

                //return delete(createSelection(item.getName()), createArg(primaryValue));
            }
            else {

                xlog.w("PrimaryKey is not defined");
                return false;
            }
        }

        return delete(createSelection(key), createArg(primaryValue)) > 0;
    }

    @SuppressLint({"DefaultLocale", "Range"})
    @Override
    @Nullable
    public String getString(long primaryValue, @NonNull String columnName) {

        var primaryKey = databaseInterface.getPrimaryKey();

        if (primaryKey == null) return null;

        var cursor = getReadableDatabase().query(
                databaseInterface.getTableName(),
                null,
                String.format("%s=?", primaryKey),
                new String[]{String.valueOf(primaryValue)},
                null,
                null,
                null
        );


        if (cursor != null) {

            String value = null;

            if (cursor.moveToFirst()) {

                value = cursor.getString(cursor.getColumnIndex(columnName));
            }

            cursor.close();
            return value;
        }

        return null;
    }

    @Override
    @Nullable
    @SuppressLint("Range")
    public long[] getLongs(long primaryValue, @NonNull String[] columnNames) {

        var primaryKey = databaseInterface.getPrimaryKey();

        if (primaryKey == null) return null;

        var cursor = getReadableDatabase().query(
                databaseInterface.getTableName(),
                null,
                String.format("%s=?", primaryKey),
                new String[]{String.valueOf(primaryValue)},
                null,
                null,
                null
        );


        if (cursor != null) {

            int    index = 0;
            long[] longs = new long[columnNames.length];

            if (cursor.moveToFirst()) {

                for (int i = 0; i < columnNames.length; i++) {

                    longs[i] = cursor.getLong(cursor.getColumnIndex(columnNames[index++]));
                }
            }

            cursor.close();

            xlog.w(Arrays.toString(longs));

            return longs;
        }

        return null;
    }

    @Override
    @Nullable
    public T find(long primaryValue) {

        return find(primaryValue, this::createObject);
    }

    @Override
    @Nullable
    public T find(long primaryValue, @NonNull Function<? super Cursor, ? extends T> func) {

        String key = databaseInterface.getPrimaryKey();

        if (key != null) {

            return find(key + "=?", new String[]{String.valueOf(primaryValue)}, func);
        }

        throw new IllegalArgumentException("Primary key not defined");
    }

    @Override
    @Nullable
    public T find(@NonNull String selection, String[] args, @NonNull Function<? super Cursor, ? extends T> func) {

        Cursor cursor = getReadableDatabase().query(
                databaseInterface.getTableName(),
                null,
                selection,
                args,
                null, null, null
        );

        if (cursor == null) return null;

        T item = null;

        if (cursor.moveToFirst()) {

            item = func.apply(cursor);
        }

        cursor.close();
        return item;
    }

    @Override
    @NonNull
    public List<T> queryAll() {

        return query(this::createObject, null, null, null);
    }

    @Override
    public List<T> queryAll(String selection) {

        return query(this::createObject, selection, null, null);
    }

    @Override
    public List<T> queryAll(String selection, String sortOrder) {

        return query(this::createObject, selection, null, sortOrder);
    }

    @NonNull
    @Override
    public List<T> queryAll(String selection, String[] selectionArgs, String sortOrder) {

        return query(this::createObject, selection, selectionArgs, sortOrder);
    }


}
