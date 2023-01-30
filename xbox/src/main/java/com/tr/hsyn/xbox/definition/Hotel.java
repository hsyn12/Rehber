package com.tr.hsyn.xbox.definition;


import com.tr.hsyn.key.Key;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


/**
 * <h2>Hotel</h2>
 * <p>
 * Misafirlerin girip kalabilecekleri basit bir otel.<br>
 * Ancak bu otelde sadece ünlüler kalır.<br>
 * Otele yerleşmek için {@link #room(Key, Object)} metodu kullanılır.
 * {@link Key} nesnesi hem bir anahtar hem de bir kimlik gibidir.
 * Otele yerleşen bir misafir, oda numarasını (anahtar) bilen her kişi için erişilebilir durumdadır.<br>
 * {@link Key} sınıfı bazı önemli anahtarları (odaları) tanımlar,
 * bu anahtarlar ile otele giriş yapan nesnelere kolayca ulaşılabilir.
 *
 * @author hsyn 7 Ocak 2023 Cumartesi 11:00
 * @see Key
 */
public abstract class Hotel {
	
	protected final Reception reception;
	
	public Hotel(@NotNull Reception reception) {
		
		this.reception = reception;
	}
	
	/**
	 * Odaya erişim sağlar.
	 *
	 * @param key Oda anahtarı
	 * @param <T> Odadaki nesne türü
	 * @return Odadaki nesne, yoksa {@code null}
	 */
	public abstract <T> T room(@NotNull Key key);
	
	/**
	 * Misafiri odaya yerleştirir.
	 *
	 * @param key    Oda anahtarı
	 * @param object Misafir nesne
	 * @param <T>    Misafir nesnenin türü
	 * @return Odada kalan önceki misafir nesne, daha önce kalan kimse yok ise {@code null}
	 */
	public abstract <T> T room(@NotNull Key key, T object);
	
	public abstract boolean exist(@NotNull Key key);
	
	/**
	 * Bir odaya bir misafirin yerleştirildiğini bildirir.
	 * Misafir ile görüşmek isteyen beklemede biri var mı kontrol eder, varsa görüştürür.
	 *
	 * @param key    Kontrol edilecek misafirin oda anahatarı
	 * @param object Kontrol edilecek misafir nesne
	 * @param <T>    Misafir nesnenin türü
	 * @return Görüşme varsa {@code true}, yoksa {@code false}
	 */
	public abstract <T> boolean peek(@NotNull Key key, T object);
	
	/**
	 * Oteldeki bir misafir ile bir görüşme talebi kaydı oluşturur.
	 * Eğer görüşülmek istenen kişi odasında ise görüşme gerçekleşir,
	 * odasında değilse ziyaretçi beklemeye alınır ve ada sahibi geldiğinde görüşme gerçekleştirilir.
	 *
	 * @param key      Görüşülmek istenen nesnenin odası
	 * @param consumer Görüşme talebinde bulunan ziyaretçi
	 * @param <T>      Misafir nesnenin türü
	 */
	public abstract <T> void meet(@NotNull Key key, Consumer<T> consumer);
	
	/**
	 * Misafirin odadan çıkış yapmasını sağlar.
	 *
	 * @param key Oda anahtarı
	 * @param <T> Misafir nesne türü
	 * @return Çıkış yapan misafir nesne, oda boş ise {@code null}
	 */
	public abstract <T> T exitFromHotel(@NotNull Key key);
	
	/**
	 * Bir misafire ait tüm görüşme taleplerini iptal eder.
	 *
	 * @param key Misafirin odası
	 * @param <T> Misafir
	 * @return Misafir ile görüşmeyi bekleyen ziyaretçilerin listesi
	 */
	public abstract <T> T cancelMeeting(@NotNull Key key);
	
	/**
	 * Oteli kapatır
	 */
	public abstract void closeTheHotel();
	
	/**
	 * Tüm görüşme taleplerini iptal eder.
	 */
	public abstract void cancelAllMeetings();
}
