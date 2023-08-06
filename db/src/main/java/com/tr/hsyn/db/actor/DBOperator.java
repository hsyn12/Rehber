package com.tr.hsyn.db.actor;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.identity.Identity;
import com.tr.hsyn.registery.Values;
import com.tr.hsyn.registery.cast.DB;
import com.tr.hsyn.registery.cast.Database;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


/**
 * Veri tabanını ve üzerinde yapılacak işlemleri tanımlar.<br>
 * En temel sınıf durumdadır ve veri tabanını oluşturmak ve en temel 4 işlemi
 * sunmakla yetinir. Bu işlemler, sorgulama, ekleme, silme, güncelleme.
 *
 * @param <T> Veri tabanına yazılacak nesne türü
 * @author hsyn 02 Nisan 2021 Cuma 12:21:54
 */
public abstract class DBOperator<T extends Identity> extends SQLiteOpenHelper implements Database<T> {
	
	protected final DB      databaseInterface;
	private final   Context context;
	
	public DBOperator(@Nullable Context context, @NonNull DB dbInterface) {
		
		super(context, dbInterface.getDatabaseName(), null, dbInterface.getVersion());
		this.databaseInterface = dbInterface;
		this.context           = context;
	}
	
	@Override
	public void onCreate(@NotNull SQLiteDatabase sqLiteDatabase) {
		
		@NotNull String table = databaseInterface.getCreateTableQuery();
		xlog.i("Creating table : %s", table);
		sqLiteDatabase.execSQL(table);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		
	}
	
	@Override
	@NotNull
	public DB getDatabaseInterface() {
		
		return databaseInterface;
	}
	
	@Override
	public long getSizeInBytes() {
		
		java.io.File file = context.getDatabasePath(databaseInterface.getDatabaseName());
		return (file != null) ? file.length() : -1;
	}
	
	@Override
	public long getRawCount() {
		
		return getReadableDatabase().compileStatement("select count(*) from " + databaseInterface.getTableName()).simpleQueryForLong();
	}
	
	@Override
	public long insert(@NotNull String table, @Nullable String nullColumnHack, @NotNull Values values) {
		
		return getWritableDatabase().insert(table, nullColumnHack, convertFrom(values));
	}
	
	@Override
	public int update(@NotNull String table, @NotNull Values values, @NotNull String whereClause, @Nullable String[] whereArgs) {
		
		xlog.d("Table name : %s, where=%s, whereArgs=%s", table, whereClause, Arrays.toString(whereArgs));
		
		
		return getWritableDatabase().update(table, convertFrom(values), whereClause, whereArgs);
	}
	
	@Override
	public int delete(@NotNull String table, @NotNull String whereClause, @Nullable String[] whereArgs) {
		
		return getWritableDatabase().delete(table, whereClause, whereArgs);
	}
	
	@Override
	public void beginTransaction() {
		
		getWritableDatabase().beginTransaction();
	}
	
	@Override
	public void endTransaction() {
		
		getWritableDatabase().endTransaction();
	}
	
	@Override
	public void setTransactionSuccessful() {
		
		getWritableDatabase().setTransactionSuccessful();
	}
	
	@NonNull
	public List<T> query(@NonNull Function<Cursor, ? extends T> function, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
		
		return COperator.<T>on(getReadableDatabase())
				.selection(selection)
				.selectionArgs(selectionArgs)
				.sortOrder(sortOrder)
				.objectFunction(function)
				.performQuery(databaseInterface.getTableName())
				.execute();
	}
	
	@NonNull
	public static ContentValues convertFrom(@NonNull Values values) {
		
		ContentValues valuesCopy = new ContentValues();
		
		for (var entry : values.getEntries()) {
			
			var value = entry.getValue();
			
			if (value == null) valuesCopy.putNull(entry.getKey());
			else if (value instanceof String str) valuesCopy.put(entry.getKey(), str);
			else if (value instanceof Integer i) valuesCopy.put(entry.getKey(), i);
			else if (value instanceof Long longValue) valuesCopy.put(entry.getKey(), longValue);
			else if (value instanceof Boolean boolValue) valuesCopy.put(entry.getKey(), boolValue);
			else valuesCopy.put(entry.getKey(), value.toString());
		}
		//xlog.d("Converted values : %s", valuesCopy);
		return valuesCopy;
	}
}