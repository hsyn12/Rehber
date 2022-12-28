package com.tr.hsyn.db.column;


import androidx.annotation.NonNull;

import com.tr.hsyn.db.cast.DBColumn;


/**
 * <h1>Number</h1>
 * <p>
 * {@code int} veya {@code long} türden bir kolonu temsil eder.
 *
 * @author hsyn 17 Eylül 2021 Cuma 11:13:30
 */
public class Number extends Column {
	
	private boolean autoIncrement;
	
	/**
	 * Verilen isimde yeni bir kolon oluşturur.
	 *
	 * @param name Kolonun ismi
	 */
	public Number(@NonNull String name) {
		
		super(name);
	}
	
	@Override
	@NonNull
	public DBColumn primaryKey(boolean autoIncrement) {
		
		primaryKey();
		this.autoIncrement = autoIncrement;
		return this;
	}
	
	@Override
	public boolean isAutoIncrement() {
		
		return autoIncrement;
	}
	
	/**
	 * @return Kolonun ismini, tablo oluşturmak için gerekli olan bilgileri de ekleyerek döndürür.
	 */
	@NonNull
	@Override
	public String getName() {
		
		return String.format("%s integer%s%s%s%s%s",
		                     super.getName(),
		                     isPrimaryKey() ? " primary key" : "",
		                     isAutoIncrement() ? " autoincrement" : "",
		                     isUnique() ? " UNIQUE" : "",
		                     isNotNull() ? " NOT NULL" : "",
		                     defaultValue() == null ? "" : " default " + defaultValue());
	}
}
