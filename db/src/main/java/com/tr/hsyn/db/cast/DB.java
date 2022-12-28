package com.tr.hsyn.db.cast;


import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.db.column.Number;
import com.tr.hsyn.db.column.Raw;
import com.tr.hsyn.db.column.Text;
import com.tr.hsyn.xlog.xlog;


/**
 * Veri tabanı oluşturmak için gereken temel bilgileri bildirir.<br>
 *
 * @author hsyn 17 Eylül 2021 Cuma 10:25:57
 */
public interface DB {
	
	/**
	 * @return Oluşturulacak veri tabanının ismi
	 */
	@NonNull
	String getDatabaseName();
	
	/**
	 * @return Veri tabanında oluşturulacak tablonun ismi
	 */
	@NonNull
	String getTableName();
	
	/**
	 * @return Tablo içinde oluşturulacak kolonlar
	 */
	@NonNull
	DBColumn[] getColumns();
	
	/**
	 * SQLite üzerinde veri tabanını oluşturmak için gereken sql komutu.
	 * Bu metodun uygulanmasına gerek yok.
	 *
	 * @return create query string
	 */
	@CallSuper
	@NonNull
	default String getCreateTableQuery() {
		
		StringBuilder table = new StringBuilder(String.format("CREATE TABLE IF NOT EXISTS %s (", getTableName()));
		
		DBColumn[] columns = getColumns();
		
		for (int i = 0; i < columns.length; i++) {
			
			DBColumn column = columns[i];
			
			table.append(column.getName());
			
			if (i != columns.length - 1) table.append(", ");
		}
		
		table.append(")");
		
		xlog.d("'%s' table created : %s", getTableName(), table.toString());
		return table.toString();
	}
	
	/**
	 * @return primary key
	 */
	@Nullable
	default String getPrimaryKey() {
		
		return null;
	}
	
	/**
	 * @return Database version
	 */
	default int getVersion() {
		
		return 1;
	}
	
	/**
	 * String bir bilgi tutacak yeni bir kolon döndürür.
	 *
	 * @param name Kolonun ismi
	 * @return Kolon
	 */
	@NonNull
	default DBColumn Text(@NonNull String name) {
		
		return new Text(name);
	}
	
	/**
	 * Binary bir bilgi tutacak yeni bir kolon döndürür.
	 *
	 * @param name Kolonun ismi
	 * @return Kolon
	 */
	@NonNull
	default DBColumn Raw(@NonNull String name) {
		
		return new Raw(name);
	}
	
	/**
	 * Sayısal bir bilgi tutacak yeni bir kolon döndürür.
	 *
	 * @param name Kolonun ismi
	 * @return Kolon
	 */
	@NonNull
	default DBColumn Number(@NonNull String name) {
		
		return new Number(name);
	}
	
}
