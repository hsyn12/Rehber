package com.tr.hsyn.registery.column;


import org.jetbrains.annotations.NotNull;


/**
 * <h1>Text</h1>
 *
 * <p>{@code String} türden bir kolonu temsil eder.</p>
 *
 * @author hsyn 17 Eylül 2021 Cuma 11:17:17
 */
public class Text extends Column {
	
	/**
	 * Verilen isimde yeni bir kolon oluşturur.
	 *
	 * @param name Kolonun ismi
	 */
	public Text(String name) {
		
		super(name);
	}
	
	/**
	 * @return Kolonun ismini, tablo oluşturmak için gerekli olan bilgileri de ekleyerek döndürür.
	 */
	@NotNull
	@Override
	public String getName() {
		
		return String.format("%s text%s%s%s%s",
		                     super.getName(),
		                     isPrimaryKey() ? " primary key" : "",
		                     isUnique() ? " UNIQUE " : "",
		                     isNotNull() ? " NOT NULL" : "",
		                     defaultValue() == null ? "" : " default " + defaultValue());
	}
}
