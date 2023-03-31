package com.tr.hsyn.db.actor;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.identity.Identity;
import com.tr.hsyn.registery.Value;
import com.tr.hsyn.registery.Values;
import com.tr.hsyn.registery.cast.DB;
import com.tr.hsyn.registery.cast.Database;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

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
	
	@NonNull
	public static ContentValues convertFrom(@NonNull Values values) {
		
		ContentValues  valuesCopy = new ContentValues();
		Value<String>  strings    = values.getValue(Values.TYPE_STRING);
		Value<Integer> ints       = values.getValue(Values.TYPE_INT);
		Value<Long>    longs      = values.getValue(Values.TYPE_LONG);
		Value<Boolean> bools      = values.getValue(Values.TYPE_BOOL);
		
		if (strings != null) {
			
			for (var key : strings.keySet()) {
				
				valuesCopy.put(key, strings.get(key));
			}
		}
		
		if (ints != null) {
			
			for (var key : ints.keySet()) {
				
				valuesCopy.put(key, ints.get(key));
			}
		}
		
		if (longs != null) {
			
			for (var key : longs.keySet()) {
				
				valuesCopy.put(key, longs.get(key));
			}
		}
		
		if (bools != null) {
			
			for (var key : bools.keySet()) {
				
				valuesCopy.put(key, bools.get(key));
			}
		}
		
		return valuesCopy;
	}
	
	@Override
	public void onCreate(@NotNull SQLiteDatabase sqLiteDatabase) {
		
		var table = databaseInterface.getCreateTableQuery();
		xlog.i("Creating table : %s", table);
		sqLiteDatabase.execSQL(table);
	}
	
	@Override
	@NotNull
	public DB getDatabaseInterface() {
		
		return databaseInterface;
	}
	
	@Override
	public long getRawCount() {
		
		return getReadableDatabase().compileStatement("select count(*) from " + databaseInterface.getTableName()).simpleQueryForLong();
	}
	
	@Override
	public long getSizeInBytes() {
		
		var file = context.getDatabasePath(databaseInterface.getDatabaseName());
		return (file != null) ? file.length() : -1;
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		
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
	
	@Override
	public long insert(@NotNull String table, @Nullable String nullColumnHack, @NotNull Values values) {
		
		return getWritableDatabase().insert(table, nullColumnHack, convertFrom(values));
	}
	
	@Override
	public int update(@NotNull String table, @NotNull Values values, @NotNull String whereClause, @Nullable @org.jetbrains.annotations.Nullable String[] whereArgs) {
		
		
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
}