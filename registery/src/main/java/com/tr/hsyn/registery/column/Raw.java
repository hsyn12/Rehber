package com.tr.hsyn.registery.column;


import org.jetbrains.annotations.NotNull;


public class Raw extends Column {
	
	/**
	 * Yeni bir kolon oluştur.<br>
	 *
	 * @param name Kolonun ismi
	 */
	public Raw(@NotNull String name) {
		
		super(name);
	}
	
	@NotNull
	@Override
	public String getName() {
		
		return String.format("%s blob%s%s",
		                     super.getName(),
		                     isNotNull() ? " NOT NULL" : "",
		                     defaultValue() == null ? "" : " default " + defaultValue());
	}
}
