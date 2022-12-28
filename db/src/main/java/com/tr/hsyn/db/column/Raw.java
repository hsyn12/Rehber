package com.tr.hsyn.db.column;


import androidx.annotation.NonNull;


public class Raw extends Column {
	
	/**
	 * Yeni bir kolon oluştur.<br>
	 *
	 * @param name Kolonun ismi
	 */
	public Raw(@NonNull String name) {
		
		super(name);
	}
	
	@NonNull
	@Override
	public String getName() {
		
		return String.format("%s blob%s%s",
		                     super.getName(),
		                     isNotNull() ? " NOT NULL" : "",
		                     defaultValue() == null ? "" : " default " + defaultValue());
	}
}
