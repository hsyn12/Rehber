package com.tr.hsyn.registery.cast;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Veri tabanı kolonu.<br>
 * Bir kolon tanımlayabilmek için gereken metotları bildirir.
 */
public interface DBColumn {
	
	/**
	 * Kolona yazılacak bilginin daha önce yazılmış olanlardan <b><u>farklı</u></b> olması gerektiğini bildirir.<br>
	 * Kolon için bu çağrı yapılmışsa, veri tabanında diğer herhangi bir satırda bu kolonun altına
	 * yazılmış bilgiyle aynı olan bir bilgi kaydedilmek istendiğinde işlem hata ile sonuçlanır.
	 *
	 * @return DBColumn
	 */
	@NotNull
	DBColumn unique();
	
	/**
	 * Bu kolonun tablo için id kolonu olduğunu bildirir.<br>
	 * {@link #unique()} ve {@link #notNull()} özellikleri taşır ve bu kolonun altına yazılacak bilgilerin benzersiz olması zorunludur.
	 *
	 * @return DBColumn
	 */
	@NotNull
	DBColumn primaryKey();
	
	/**
	 * Bu kolonun tablo için id kolonu olduğunu bildirir.<br>
	 * {@link #unique()} ve {@link #notNull()} özellikleri taşır ve bu kolonun altına yazılacak bilgilerin benzersiz olması zorunludur.
	 *
	 * @param autoIncrement Sayısal bir değer için otomatik arttırma yapılması isteniyorsa {@code true}
	 * @return DBColumn
	 */
	@NotNull
	DBColumn primaryKey(boolean autoIncrement);
	
	/**
	 * Veri tabana yeni satır eklenirken bu kolon es geçilebilir ve
	 * verilen değer varsayılan olarak kaydedilir.
	 *
	 * @param value Varsayılan değer
	 * @return DBColumn
	 */
	@NotNull
	DBColumn defaultValue(@Nullable String value);
	
	/**
	 * Veri tabana yeni satır eklenirken bu kolon es geçilebilir ve
	 * verilen değer varsayılan olarak kaydedilir.
	 *
	 * @param value Varsayılan değer
	 * @return DBColumn
	 */
	@NotNull
	DBColumn defaultValue(int value);
	
	/**
	 * Veri tabana yeni satır eklenirken bu kolon es geçilebilir ve
	 * verilen değer varsayılan olarak kaydedilir.
	 *
	 * @param value Varsayılan değer
	 * @return DBColumn
	 */
	@NotNull
	DBColumn defaultValue(long value);
	
	/**
	 * Kolonun asla es geçilemeyeceğini, {@code null} değer atanamayacağını ve
	 * bir bilgi girişi yapmanın zorunlu olduğunu bildirir.
	 *
	 * @return DBColumn
	 */
	@NotNull
	DBColumn notNull();
	
	/**
	 * @return Kolon için {@link #unique()} metodu çağrılmışsa {@code true}
	 */
	boolean isUnique();
	
	/**
	 * @return Kolon için {@link #primaryKey(boolean)} metodu {@code true} argümanı ile çağrılmışsa {@code true}
	 */
	boolean isAutoIncrement();
	
	/**
	 * @return Kolon için {@link #primaryKey()} yada {@link #primaryKey(boolean)} metodu çağrılmışsa {@code true}
	 */
	boolean isPrimaryKey();
	
	/**
	 * @return Kolon için {@link #notNull()} metodu çağrılmışsa {@code true}
	 */
	boolean isNotNull();
	
	/**
	 * @return Kolonun ismi ve özellikleri. Bu string tablo oluşturulurken sql komutunda kolonu tanıtmak için kullanılır.<br> Mesela '{@code time integer primary key}'
	 */
	@NotNull
	String getName();
	
	
}
