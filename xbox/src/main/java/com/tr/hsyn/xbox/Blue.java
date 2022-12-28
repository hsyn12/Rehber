package com.tr.hsyn.xbox;


import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.reflection.Clazz;
import com.tr.hsyn.xbox.message.Message;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntSupplier;


/**
 * <h2>Blue</h2>
 * <p>
 * Nesnelerin interneti.
 *
 * @author hsyn 30 Aralık 2021 Perşembe 17:32:09
 */
public final class Blue {
	
	private static final Map<Integer, Object> internet    = new HashMap<>();
	private static final Organizator          ORGANIZATOR = new Organizator();
	private static       Dispatcher           DISPATCHER;
	
	private static <T> void checkKeepClass(@NotNull T object) {
		
		var clazz = object.getClass();
		
		if (!clazz.getCanonicalName().startsWith("java.") && !clazz.isAnnotationPresent(Keep.class)) {
			
			xlog.w("Not annotated with Keep : %s", object.getClass().getCanonicalName());
		}
	}
	
	/**
	 * Nesneyi kaydet.
	 * Bu şekile kaydedilen nesne,
	 * {@linkplain Class#getCanonicalName()} değerinin hash kodu ile alınır.
	 *
	 * @param object Nesne
	 * @param <T>    Nesne türü
	 */
	public static <T> void box(T object) {
		
		if (object != null) {
			
			int key = object.getClass().getCanonicalName().hashCode();
			put(key, object);
		}
	}
	
	/**
	 * Nesneyi kaydet.
	 *
	 * @param key    Kayıt anahtarı
	 * @param object Nesne
	 * @param <T>    Nesne türü
	 */
	public static <T> void box(int key, T object) {
		
		if (object != null) {
			
			put(key, object);
		}
	}
	
	/**
	 * Verilen sınıfın kayıtlı nesnesi varsa döndürülür.
	 * Yoksa, nesne oluşturulur kaydedilir ve döndürülür.
	 * {@linkplain #single(Class, Object...)} metodunun diğer bir adı bu.
	 *
	 * @param clazz Sınıf
	 * @param <T>   Sınıf türü
	 * @return Sınıf nesnesi
	 */
	@Nullable
	public static <T> T box(@NotNull Class<T> clazz, @Nullable Object... params) {
		
		return single(clazz, params);
	}
	
	/**
	 * Buluşma ayarla.
	 * Eğer verilen sınıfa ait nesne daha önce kaydedilmişse
	 * buluşma hemen gerçekleşir.
	 * Kaydedilmemişse takipçi beklemeye alınır ve
	 * beklenen nesne kayıt edildiğinde buluşma gerçekleştirilir.
	 *
	 * @param clazz    Sınıf
	 * @param follower Takipçi
	 * @param <T>      Sınıf türü
	 */
	public static <T> void meet(@NotNull Class<T> clazz, @NotNull Consumer<T> follower) {
		
		meet(getKey(clazz), follower);
	}
	
	private static <T> int getKey(@NotNull Class<T> clazz) {
		
		return clazz.getCanonicalName().hashCode();
	}
	
	/**
	 * Verilen anahtara ait nesneyle buluşturur.
	 * Eğer nesne yoksa takibe alır ve ilk fırsatta buluşma sağlanır.
	 *
	 * @param key      Buluşmak istenilen nesnin kayıt anahtarı
	 * @param follower Buluşmak isteyen kişi
	 * @param <T>      Nesne tüt
	 */
	public static <T> void meet(int key, @NotNull Consumer<T> follower) {
		
		T obj = getObject(key);
		
		if (obj != null) {
			
			follower.accept(obj);
		}
		else {
			
			ORGANIZATOR.addFollower(key, follower);
		}
	}
	
	public static <T> void meet(@NotNull IntSupplier keySupplier, @NotNull Consumer<T> follower) {
		
		meet(keySupplier.getAsInt(), follower);
	}
	
	/**
	 * Buluşmayı iptal et.
	 * Bu çağrıdan sonra, verilen anahtar için kaydedilmiş tüm buluşmalar iptal edilir.
	 *
	 * @param key Takip edilen nesnenin kayıt anahtarı
	 */
	public static void breakeMetting(int key) {
		
		ORGANIZATOR.unFollow(key);
	}
	
	/**
	 * Verilen sınıfın nesnesini oluştur.
	 * Oluştur fırlat, kayıt yok.
	 *
	 * @param clazz Sınıf
	 * @param <T>   Sınıf türü
	 * @return Sınıf nesnesi
	 */
	@Nullable
	public static <T> T make(Class<T> clazz) {
		
		return Clazz.create(clazz);
	}
	
	/**
	 * Verilen sınıfın nesnesini oluştur.
	 * Oluştur fırlat, kayıt yok.
	 *
	 * @param clazz  Sınıf
	 * @param params Kurucu argümanları
	 * @param <T>    Sınıf türü
	 * @return Sınıf nesnesi
	 */
	@Nullable
	public static <T> T make(@NotNull Class<T> clazz, @Nullable Object... params) {
		
		return Clazz.create(clazz, params);
	}
	
	/**
	 * Verilen sınıfa ait nesne daha önce kaydedilmişse döndür.
	 * Kaydedilmemişse oluştur kaydet ve döndür.
	 *
	 * @param clazz  Sınıf
	 * @param params Kurucu argümanları
	 * @param <T>    Sınıf türü
	 * @return Sınıf nesnesi
	 */
	public static <T> T single(@NotNull Class<T> clazz, @Nullable Object... params) {
		
		int key = getKey(clazz);
		T   obj = getObject(key);
		
		//! Burada buluşma yok çünkü nesne talep ediliyor
		if (obj != null) return obj;
		
		return put(key, make(clazz, params));
	}
	
	/**
	 * Nesneyi döndür.
	 * Döndürülen nesnenin kaydı devam eder.
	 *
	 * @param key Nesnenin kayıt anahtarı
	 * @param <T> Nesnenin türü
	 * @return Nesne. Yoksa {@code null}.
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T getObject(int key) {return (T) internet.get(key);}
	
	/**
	 * Bir anahtarla bir nesneyi kaydet.
	 * Bu anahtar zaten varsa yeni nesne ile güncellenir.
	 * <p>
	 * Bu çağrı aynı zamanda buluşma kontrolü yapar.
	 * Eğer bir buluşma varsa nesne takipçisiyle buluşturulur ve silinir.
	 *
	 * @param key Anahtar
	 * @param obj Nesne
	 * @param <T> Nesne türü
	 * @return Kaydedilen nesne
	 */
	@Nullable
	public static <T> T put(int key, T obj) {
		
		if (obj != null) {
			
			checkKeepClass(obj);
			
			//Buluşmayı kontrol et
			if (!ORGANIZATOR.meet(key, obj)) {
				
				//! Bir nesnenin buluşması varsa tek kullanımlık gibi iş görür
				//! Kullanılıp silinir
				//! Bu biraz karışıklık yaratabilir
				//! Saklanmak istenen nesne kalıcı olmalı ise ve nesnenin buluşması varsa saklanmıyor
				//! Bu durumda buluşmak isteyen kişi, nesnenin saklanmasından sonra talepte bulunmak zorunda
				//! Fuck you!!
				
				//- Buluşma yoksa sakla
				internet.put(key, obj);
				
				//+ Buluşma taleplerinin nesneleri geçici nesneler olarak düşünülmeli
				//+ Her nesne kalıcı olarak saklanırsa burası çöplüğe döner
			}
			
			return obj;
		}
		
		return null;
	}
	
	/**
	 * Verilen anahtarla kaydedilen nesneyi sil ve döndür.
	 * Böyle bir kayıt yoksa {@code null} döner.
	 *
	 * @param key Kayıt anahtarı
	 * @param <T> Nesnenin türü
	 * @return Nesne
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T remove(int key) {
		
		return (T) internet.remove(key);
	}
	
	/**
	 * Nesneyi sil ve döndür.
	 * Kayıtlı değilse {@code null} döner.
	 *
	 * @param obj Nesne
	 * @param <T> Nesnenin türü
	 * @return Nesne
	 */
	@Nullable
	public static <T> T remove(Object obj) {
		
		if (obj != null) {
			
			return remove(obj.getClass().getCanonicalName().hashCode());
		}
		
		return null;
	}
	
	/**
	 * Tüm kayıtları sil
	 */
	public static void clear() {
		
		internet.clear();
		ORGANIZATOR.clear();
		
		xlog.i("All clear");
	}
	
	public static <O> O stream(@NotNull Data data) {
		
		return DISPATCHER.dispatch(data);
	}
	
	public static void message(@NotNull Message message) {
		
		stream(Data.create(Key.MESSAGE, message));
	}
	
	public static void infoMessage(@NotNull CharSequence from, @NotNull CharSequence message) {
		
		stream(Data.create(Key.MESSAGE, Message.info(from, message)));
	}
	
	public static void warnMessage(@NotNull CharSequence from, @NotNull CharSequence message) {
		
		stream(Data.create(Key.MESSAGE, Message.warn(from, message)));
	}
	
	public static void importantMessage(@NotNull CharSequence from, @NotNull CharSequence message) {
		
		stream(Data.create(Key.MESSAGE, Message.important(from, message)));
	}
}