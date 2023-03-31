package com.tr.hsyn.registery.cast;


import com.tr.hsyn.registery.column.Number;
import com.tr.hsyn.registery.column.Raw;
import com.tr.hsyn.registery.column.Text;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;


/**
 * Veri tabanı oluşturmak için gereken temel bilgileri sağlama konusunda klavuzluk yapar.<br>
 *
 * @author hsyn 17 Eylül 2021 Cuma 10:25:57
 */
public interface DB {
	
	/**
	 * String bir bilgi tutacak yeni bir kolon döndürür.
	 *
	 * @param name Kolonun ismi
	 * @return Kolon
	 */
	@NotNull
	static DBColumn text(@NotNull String name) {
		
		return new Text(name);
	}
	
	/**
	 * Binary bir bilgi tutacak yeni bir kolon döndürür.
	 *
	 * @param name Kolonun ismi
	 * @return Kolon
	 */
	@NotNull
	static DBColumn raw(@NotNull String name) {
		
		return new Raw(name);
	}
	
	/**
	 * Sayısal bir bilgi tutacak yeni bir kolon döndürür.
	 *
	 * @param name Kolonun ismi
	 * @return Kolon
	 */
	@NotNull
	static DBColumn number(@NotNull String name) {
		
		return new Number(name);
	}
	
	/**
	 * @return Oluşturulacak veri tabanının ismi
	 */
	@NotNull
	String getDatabaseName();
	
	/**
	 * @return Veri tabanında oluşturulacak tablonun ismi
	 */
	@NotNull
	String getTableName();
	
	/**
	 * @return Tablo içinde oluşturulacak kolonlar
	 */
	@NotNull
	DBColumn[] getColumns();
	
	/**
	 * @return primary key
	 */
	@NotNull
	String getPrimaryKey();
	
	/**
	 * SQLite üzerinde veri tabanını oluşturmak için gereken sql komutu.
	 * Bu metodun uygulanmasına gerek yok.
	 *
	 * @return create query string
	 */
	@NotNull
	default String getCreateTableQuery() {
		
		StringBuilder table = new StringBuilder(String.format("CREATE TABLE %s (", getTableName()));
		
		DBColumn[] columns = getColumns();
		
		for (int i = 0; i < columns.length; i++) {
			
			DBColumn column = columns[i];
			
			table.append(column.getName());
			
			if (i != columns.length - 1) table.append(", ");
		}
		
		table.append(")");
		
		xlog.i("'%s' table created : %s", getTableName(), table);
		return table.toString();
	}
	
	/**
	 * @return Database version
	 */
	default int getVersion() {
		
		return 1;
	}
	
}
