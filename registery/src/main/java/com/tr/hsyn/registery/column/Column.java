package com.tr.hsyn.registery.column;


import com.tr.hsyn.registery.cast.DBColumn;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * <h1>Column</h1>
 *
 * <p>Veri tabanında bir kolonu temsil eder.</p>
 *
 * @author hsyn 17 Eylül 2021 Cuma 10:39:31
 */
public abstract class Column implements DBColumn {
	
	/**
	 * Kolonun ismi.
	 * Bu isme kolonun özellikleri de dahil.
	 * Alt sınıflar kolon ismini verirken özellikleri de ekler.
	 * mesela {@code not null, default 0} gibi.
	 * Ancak bu sınıf temel sınıf olduğu için sadece yalın ismi tutar.
	 */
	@NotNull
	private final String  name;
	/**
	 * Kolonun primaryKey olduğunu bildirir.
	 */
	private       boolean primaryKey;
	private       boolean unique;
	/**
	 * Kolonun {@code null} olamayacağını bildirir.
	 */
	private       boolean notNull;
	/**
	 * Kolonun varsayılan değerini belirtir.
	 */
	@Nullable
	private       String  defaultValue;
	
	/**
	 * Yeni bir kolon oluştur.<br>
	 * ---------------------------------------------------------------------------<br>
	 * Kolon oluşturma işlemi alt sınıflarla yapılır.
	 * Bu sınıf sadece temel bilgileri oluşturur,
	 * {@code primaryKey, notNull, defaultValue} gibi.
	 * Ancak bu özellikleri tablo oluştururken anlamlı hale
	 * getirecek olan alt sınıflardır.
	 * Alt sınıflar kolon için tür belirtmek zorundalar.
	 * Ancak bu temel sınıf kolon türü bildirmez.
	 * Bu yüzden tablo oluşturmak için direk bu kurucu <b><u>kullanılamaz.</u></b>
	 * Bu yüzden bu sınıf soyut ve bu yüzden bu kurucu korumaya alındı.
	 *
	 * @param name Kolonun ismi
	 */
	protected Column(@NotNull String name) {
		
		this.name = name;
	}
	
	/**
	 * @return Kolonun varsayılan değerini döndürür. Eğer varsayılan bir değer belirtilmemişse {@code null} döndürür.
	 */
	@Nullable
	protected String defaultValue() {
		
		return defaultValue;
	}
	
	@NotNull
	@Override
	public DBColumn unique() {
		
		this.unique = true;
		return this;
	}
	
	@Override
	@NotNull
	public DBColumn primaryKey(boolean autoIncrement) {
		
		return this;
	}
	
	/**
	 * Kolona varsayılan bir değer atar.
	 *
	 * @param value Varsayılan değer
	 * @return Üzerinde işlem yapılan kolonu döndürür.
	 */
	@Override
	@NotNull
	public DBColumn defaultValue(@Nullable String value) {
		
		defaultValue = value;
		return this;
	}
	
	/**
	 * Kolona varsayılan bir değer atar.
	 *
	 * @param value Varsayılan değer
	 * @return Üzerinde işlem yapılan kolonu döndürür.
	 */
	@Override
	@NotNull
	public DBColumn defaultValue(int value) {
		
		defaultValue = String.valueOf(value);
		return this;
	}
	
	/**
	 * Kolona varsayılan bir değer atar.
	 *
	 * @param value Varsayılan değer
	 * @return Üzerinde işlem yapılan kolonu döndürür.
	 */
	@Override
	@NotNull
	public DBColumn defaultValue(long value) {
		
		defaultValue = String.valueOf(value);
		return this;
	}
	
	/**
	 * Kolonu {@code primary key} olarak bildirir.
	 *
	 * @return Üzerinde işlem yapılan kolonu döndürür.
	 */
	@Override
	@NotNull
	public DBColumn primaryKey() {
		
		primaryKey = true;
		return this;
	}
	
	/**
	 * Kolonu {@code not null} olarak bildirir.
	 *
	 * @return Üzerinde işlem yapılan kolonu döndürür.
	 */
	@Override
	@NotNull
	public DBColumn notNull() {
		
		this.notNull = true;
		return this;
	}
	
	@Override
	public boolean isUnique() {
		
		return unique;
	}
	
	@Override
	public boolean isAutoIncrement() {
		
		return false;
	}
	
	/**
	 * @return Kolonun {@code primary key} olarak belirtilip belirtilmediğini bildirir.
	 *      {@code true} ise {@code primary key} olarak belirtilmiş demektir.
	 * 		Bir tabloda sadece bir tane kolon {@code primary key} olarak belirtilebilir.
	 * 		Birden fazla kolon {@code primary key} olarak belirtilmiş ise tablo oluşturma işlemi
	 * 		başarısız olur.
	 */
	@Override
	public boolean isPrimaryKey() {
		
		return primaryKey;
	}
	
	/**
	 * @return Kolonun {@code not null} olarak belirtilip belirtilmediğini bildirir.
	 * 		true ise {@code not null} olarak belirtilmiş demektir.
	 * 		Bir tabloda birden fazla kolon {@code not null} olarak belirtilebilir.
	 */
	@Override
	public boolean isNotNull() {
		
		return notNull;
	}
	
	/**
	 * @return Kolona verilen ismi döndürür.
	 */
	@Override
	@NotNull
	public String getName() {
		
		return name;
	}
	
	
	@Override
	public String toString() {
		
		return getName();
	}
}
