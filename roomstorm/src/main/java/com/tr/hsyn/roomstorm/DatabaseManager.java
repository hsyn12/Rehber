/*
 * Copyright 2015 Dimitry Ivanov (mail@dimitryivanov.ru)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tr.hsyn.roomstorm;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.roomstorm.op.Delete;
import com.tr.hsyn.roomstorm.op.Insert;
import com.tr.hsyn.roomstorm.op.Select;
import com.tr.hsyn.roomstorm.op.Transactional;
import com.tr.hsyn.roomstorm.op.Update;
import com.tr.hsyn.roomstorm.query.Selection;
import com.tr.hsyn.roomstorm.util.CacheBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Class that holds a reference and connection for SQLite database.
 * It could be easily used with multiple database files. As long as all SQLite operations
 * ({@link Select}, {@link Insert},
 * {@link Update} and {@link Delete}) are constructed
 * with instance of this class ({@link Storm#newSelect(DatabaseManager)},
 * {@link Storm#newInsert(DatabaseManager)}, {@link Storm#newUpdate(DatabaseManager)},
 * {@link Storm#newDelete(DatabaseManager)}) it's save to create as much database
 * files as you like.
 * <p>
 * This class has no synchronization whatsoever
 * <p>
 * Each instance of this class holds it's own cache
 * <p>
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) 01.02.15.
 */
public class DatabaseManager implements Transactional {

    private final ContentResolver            mResolver;
    private final DBInfo                     mInfo;
    private final Map<Class<?>, CachedTable> mCached;

    private final CachedTableGetter mCachedTableGetter;

    private SQLiteDatabase mDataBase;

    /**
     * @see #DatabaseManager(Context, String, int, Class[], Pragma)
     */
    public DatabaseManager(
            @NonNull Context context,
            @NonNull String dbName,
            int dbVersion,
            @NonNull Class<?>[] classes
    ) {

        this(context, dbName, dbVersion, classes, null);
    }

    /**
     * Prepares <code>DatabaseManager</code>
     *
     * @param context   {@link Context} (prefer {@link android.app.Application} <code>context</code>)
     * @param dbName    the name for db
     * @param dbVersion teh version for db
     * @param classes   an array of classes in this table
     * @param pragma    {@link Pragma} statements to be executed
     *                  when SQLite database connection is opened
     */
    public DatabaseManager(
            @NonNull Context context,
            @NonNull String dbName,
            int dbVersion,
            @NonNull Class<?>[] classes,
            @Nullable Pragma pragma
    ) {

        this.mResolver = context.getContentResolver();
        this.mInfo     = new DBInfo(dbName, dbVersion, classes, pragma);

        final CacheBuilder cacheBuilder = new CacheBuilder(context.getPackageName(), dbName);
        mCached = cacheBuilder.buildCache(classes);

        this.mCachedTableGetter = new CachedTableGetter();
    }

    /**
     * @see #open(SQLiteOpenCallbacks)
     */
    public void open() throws SQLiteException {

        open(null);
    }

    /**
     * Opens new SQLite connection and closes previous if it was not closed
     *
     * @param callbacks {@link SQLiteOpenCallbacks} to be called
     *                  after main <code>create</code>, <code>update</code> and <code>open</code>
     *                  methods ware executed
     * @throws SQLiteException if there was an error during operation
     */
    public void open(@Nullable SQLiteOpenCallbacks callbacks) throws SQLiteException {

        if (isOpen()) {
            close();
        }
        initDB(Storm.getApplicationContext(), mInfo.name, mInfo.version, new ArrayList<>(mCached.values()), mInfo.pragma, callbacks);
    }

    private void initDB(
            Context context,
            String dbName,
            int dbVersion,
            List<CachedTable> tables,
            @Nullable Pragma pragma,
            @Nullable SQLiteOpenCallbacks openCallbacks
    ) throws SQLiteException {

        StormOpenHelper openHelper = new StormOpenHelper(context, dbName, dbVersion, tables, pragma, openCallbacks);
        mDataBase = openHelper.getWritableDatabase();
    }

    /**
     * Generally should not be used directly
     *
     * @param clazz to be queried for tables cache
     * @return {@link CachedTable}
     * @see CachedTableGetter#getCachedTable(Class)
     */
    public @NonNull
    CachedTable getCachedTable(Class<?> clazz) {

        return mCachedTableGetter.getCachedTable(clazz);
    }

    /**
     * Generally should not be used directly
     *
     * @return {@link List} of all cached tables
     */
    public @NonNull
    List<CachedTable> getCachedTables() {

        return new ArrayList<>(mCached.values());
    }

    /**
     * Returns backed {@link SQLiteDatabase}
     *
     * @return {@link SQLiteDatabase}
     */
    public SQLiteDatabase getDataBase() {

        return mDataBase;
    }

    /**
     * @return name of this database file
     */
    public String getDataBaseName() {

        return mInfo.name;
    }

    /**
     * @param uri to be notified
     * @see ContentResolver#notifyChange(Uri, android.database.ContentObserver)
     */
    public void notifyChange(Uri uri) {

        mResolver.notifyChange(uri, null, ContentResolver.NOTIFY_SYNC_TO_NETWORK);
    }

    /**
     * @param clazz table to be notified
     * @see #notifyChange(Uri)
     */
    public void notifyChange(Class<?> clazz) {

        notifyChange(getCachedTable(clazz).getNotificationUri());
    }

    /**
     * Closes currently open connection
     */
    public void close() {

        mDataBase.close();
    }

    /**
     * @return <code>true</code> if connection is open, <code>false</code> otherwise
     */
    public boolean isOpen() {

        return mDataBase != null && mDataBase.isOpen();
    }

    /**
     * @see SQLiteDatabase#insert(String, String, ContentValues)
     */
    public long insert(@NonNull String tableName, @NonNull ContentValues contentValues) {

        return mDataBase.insert(tableName, null, contentValues);
    }

    public long insert(@NonNull String tableName, @NonNull ContentValues contentValues, int onConflict) {

        return mDataBase.insertWithOnConflict(tableName, null, contentValues, onConflict);
    }

    /**
     * @see #insert(String, ContentValues)
     */
    public long insert(Class<?> clazz, @NonNull ContentValues cv) {

        return insert(getTableName(clazz), cv);
    }

    /**
     * @see #update(String, Selection, ContentValues)
     */
    public int update(Class<?> clazz, @Nullable Selection selection, @NonNull ContentValues contentValues) {

        return update(getTableName(clazz), selection, contentValues);
    }

    /**
     * @see SQLiteDatabase#update(String, ContentValues, String, String[])
     */
    public int update(String tableName, @Nullable Selection selection, @NonNull ContentValues cv) {

        final boolean hasSelection = selection != null;
        return mDataBase.update(
                tableName,
                cv,
                hasSelection ? selection.getSelection() : null,
                hasSelection ? selection.getSelectionArgs() : null
        );
    }

    /**
     * @see #delete(String, Selection)
     */
    public int delete(Class<?> clazz, @Nullable Selection selection) {

        return delete(getTableName(clazz), selection);
    }

    /**
     * @see SQLiteDatabase#delete(String, String, String[])
     */
    public int delete(String tableName, @Nullable Selection selection) {

        final boolean hasSelection = selection != null;
        return mDataBase.delete(
                tableName,
                hasSelection ? selection.getSelection() : null,
                hasSelection ? selection.getSelectionArgs() : null
        );
    }

    /**
     * @return {@link Uri} for specified table
     */
    public @NonNull
    Uri getNotificationUri(Class<?> clazz) {

        return getCachedTable(clazz).getNotificationUri();
    }

    /**
     * @see #count(Class, Selection)
     */
    public boolean has(Class<?> clazz, Selection selection) {

        return count(clazz, selection) > 0;
    }

    /**
     * @see #count(Class, Selection)
     */
    public int count(Class<?> clazz) {

        return count(clazz, null);
    }

    /**
     * Counts number of rows in specified query. If {@link Selection} is null
     * then counts all rows in Class table.
     *
     * @param clazz     table class
     * @param selection {@link Selection}
     * @return number of rows from executed query
     */
    public int count(Class<?> clazz, @Nullable Selection selection) {

        final String tableName = getTableName(clazz);

        final boolean hasSelection = selection != null;

        final Cursor cursor = mDataBase.query(
                tableName,
                new String[]{"count(1)"},
                hasSelection ? selection.getSelection() : null,
                hasSelection ? selection.getSelectionArgs() : null,
                null,
                null,
                null,
                "1"
        );

        if (cursor == null) {
            return 0;
        }

        final int result;

        if (cursor.moveToFirst()) {
            result = cursor.getInt(0);
        }
        else {
            result = 0;
        }

        cursor.close();
        return result;
    }

    /**
     * @see #query(String, String[], String, String[], String, String, String, String)
     */
    public Cursor query(@NonNull Query query) {

        final String tableName = getTableName(query);

        return query(
                tableName,
                query.getColumns(),
                query.getSelection(),
                query.getSelectionArgs(),
                query.getGroupBy(),
                query.getHaving(),
                query.getOrderBy(),
                query.getLimit()
        );
    }

    /**
     * @see SQLiteDatabase#query(String, String[], String, String[], String, String, String, String)
     */
    public Cursor query(
            @NonNull String tableName,
            String[] columns,
            String selection,
            String[] selectionArgs,
            String groupBy,
            String having,
            String orderBy,
            String limit
    ) {

        return mDataBase.query(
                tableName,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy,
                limit
        );
    }

    /**
     * @see SQLiteDatabase#rawQuery(String, String[])
     */
    public Cursor query(String sql, String[] ags) {

        return mDataBase.rawQuery(sql, ags);
    }

    private @NonNull
    String getTableName(Query query) {

        if (!TextUtils.isEmpty(query.getTableName())) {
            return query.getTableName();
        }

        final Class<?> clazz = query.getTableClass();
        if (clazz == null) {
            throw new IllegalArgumentException("Query must be supplied with a tableName or a tableClass");
        }

        return getTableName(clazz);
    }

    /**
     * @param clazz table class
     * @return {@link String} of the specified table
     */
    public @NonNull
    String getTableName(Class<?> clazz) {

        return getCachedTable(clazz).getTableName();
    }

    /**
     * @see #getTableName(Class)
     */
    public @NonNull
    String getTableName(Object who) {

        return getTableName(who.getClass());
    }

    /**
     * @see Transactional#beginTransaction()
     */
    @Override
    public void beginTransaction() {

        mDataBase.beginTransaction();
    }

    /**
     * @see Transactional#setTransactionSuccessful()
     */
    @Override
    public void setTransactionSuccessful() {

        mDataBase.setTransactionSuccessful();
    }

    /**
     * @see Transactional#endTransaction()
     */
    @Override
    public void endTransaction() {

        mDataBase.endTransaction();
    }

    private static class DBInfo {

        private final String     name;
        private final int        version;
        private final Class<?>[] tables;
        private final Pragma     pragma;

        DBInfo(
                String name,
                int version,
                Class<?>[] tables,
                @Nullable Pragma pragma
        ) {

            this.name    = name;
            this.version = version;
            this.tables  = tables;
            this.pragma  = pragma;
        }
    }

    private class CachedTableGetter {

        @NonNull
        CachedTable getCachedTable(Class<?> clazz) {

            final CachedTable table = mCached.get(clazz);
            if (table == null) {
                throw new NullPointerException("No CachedTable found for a class: " + clazz
                                               + ". Is it annotated with @Table and/or passed to a DatabaseManager instance?");
            }
            return table;
        }

    }
}
