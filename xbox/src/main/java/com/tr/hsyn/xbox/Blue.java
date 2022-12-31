package com.tr.hsyn.xbox;


import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.reflection.Clazz;
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
 * Nesnelerin interneti.<br>
 * Bu sınıfın görevi nesneleri kaydetmek.<br>
 * Nesneler bir anahtar ile kaydedilir,
 * ve nesnenin kullanıcıları kayıt anahtarı ile kaydedilen nesneye ulaşır.<br><br>
 *
 * <pre>
 * Blue.box(getContext());
 * Context context = Blue.getObject(Context.class);
 * // Yada
 * Blue.box(999, getContext());
 * Context context = Blue.getObject(999);
 * </pre>
 * <p>
 * Eğer istenen nesne o an kayıtlı değilse ve
 * kaydedildiğinde haberdar olmak ve nesneye erişmek isteniyorsa {@code meet} metotları kullanılır.<br><br>
 *
 * <pre>
 * Blue.meet(999, context -> context.bindService(...))
 * </pre>
 * <p>
 * Çağrı yapıldığında, istenen nesne kayıtlı ise derhal erişim sağlanır.
 *
 * @author hsyn 30 Aralık 2021 Perşembe 17:32:09
 */
public final class Blue {
	
	private static final Map<Integer, Object> internet    = new HashMap<>();
	private static final Organizator          ORGANIZATOR = new Organizator();
	
	private static <T> void checkKeepClass(@NotNull T object) {
		
		var clazz = object.getClass();
		
		if (!clazz.getCanonicalName().startsWith("java.") && !clazz.isAnnotationPresent(Keep.class)) {
			
			xlog.w("Not annotated with Keep : %s", object.getClass().getCanonicalName());
		}
	}
	
	/**
	 * Nesneyi kaydeder.<br>
	 * Bu şekile kaydedilen nesne,
	 * {@linkplain Class#getCanonicalName()} değerinin hash kodu ile alınır.<br><br>
	 *
	 * <pre>
	 * Context context = Blue.get(Context.class.getCanonicalName());
	 * Context context = Blue.get(context.getClass().getCanonicalName());
	 * </pre>
	 *
	 * @param object Nesne
	 * @param <T>    Nesne türü
	 */
	public static <T> void box(T object) {
		
		putObject(getKey(object), object);
	}
	
	/**
	 * Nesneyi kaydet.<br>
	 * Bu şekilde kaydedilen nesne, verilen {@code key} ile geri alınır.<br><br>
	 *
	 * <pre>
	 * Context context = Blue.get(key);
	 * </pre>
	 *
	 * @param key    Kayıt anahtarı
	 * @param object Nesne
	 * @param <T>    Nesne türü
	 */
	public static <T> void box(int key, T object) {
		
		putObject(key, object);
	}
	
	/**
	 * Verilen sınıfın kayıtlı bir nesnesi varsa döndürülür.
	 * Yoksa, nesne oluşturulur kaydedilir ve döndürülür.
	 *
	 * @param clazz Sınıf
	 * @param <T>   Sınıf türü
	 * @return Sınıf nesnesi
	 */
	@Nullable
	public static <T> T box(@NotNull Class<T> clazz, @Nullable Object... params) {
		
		int key = getKey(clazz);
		T   obj = getObject(key);
		
		if (obj != null) return obj;//! Burada buluşma yok çünkü nesne talep ediliyor
		
		return putObject(key, Clazz.create(clazz, params));
	}
	
	/**
	 * Bir sınıf için anahtar döndürür.<br>
	 * Bu anahtar, sınıfın  {@linkplain Class#getCanonicalName()} metodundan
	 * dönen değerin hash kodudur.
	 *
	 * @param clazz Sınıf
	 * @param <T>   Sınıf türü
	 * @return Anahtar
	 */
	public static <T> int getKey(Class<T> clazz) {
		
		if (clazz != null)
			return clazz.getCanonicalName().hashCode();
		
		return 0;
	}
	
	/**
	 * Bir nesne için anahtar döndürür.<br>
	 * Bu anahtar, nesnenin  {@linkplain Class#getCanonicalName()} metodundan
	 * dönen değerin hash kodudur.
	 *
	 * @param object Nesne
	 * @param <T>    Nesne türü
	 * @return Anahtar
	 */
	public static <T> int getKey(Object object) {
		
		if (object != null)
			return object.getClass().getCanonicalName().hashCode();
		
		return 0;
	}
	
	/**
	 * Buluşma ayarlar.<br>
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
	
	/**
	 * Verilen anahtarla kayıtlı nesneyi takipçi ile buluşturur.<br>
	 * Eğer nesne yoksa takipçi beklemeye alınır (kaydedilir) ve ilk fırsatta buluşma sağlanır.<br>
	 * Bu buluşma tek seferliktir ve gerçekleştiğinde, takipçi ile ilgili
	 * hiç bir kayıtlı bilgi kalmaz.<br>
	 * Bu metodun amacı, bir nesnenin herhangi bir zamanda erişimini mümkün kılmak.<br>
	 * Çünkü bir nesne talep edildiği anda kayıt edilmemiş olabilir.
	 * Bu yüzden {@link #getObject(int)} metodu çağrıldığında nesne kayıtlı değilse {@code null} döner.<br>
	 *
	 * @param key      Buluşmak istenilen nesnin kayıt anahtarı
	 * @param follower Buluşmak isteyen kişi
	 * @param <T>      Nesne türü
	 */
	public static <T> void meet(int key, @NotNull Consumer<T> follower) {
		
		T obj = getObject(key);
		
		if (obj != null) {
			
			//- Nesne kayıtlı ise hemen buluştur ve olay bitsin
			follower.accept(obj);
		}
		else {
			
			//- Aranan nesne kayıtlı değil
			//- Takip başlasın
			ORGANIZATOR.follow(key, follower);
		}
	}
	
	/**
	 * Verilen anahtara ait nesneyle buluşturur.<br>
	 * Eğer nesne yoksa takibe alır ve ilk fırsatta buluşma sağlanır.
	 *
	 * @param keySupplier Anahtar sağlayıcısı
	 * @param follower    Takipçi
	 * @param <T>         Takip edilen nesnenin türü
	 */
	public static <T> void meet(@NotNull IntSupplier keySupplier, @NotNull Consumer<T> follower) {
		
		meet(keySupplier.getAsInt(), follower);
	}
	
	/**
	 * Buluşma talebini iptal eder.<br>
	 * Bu çağrıdan sonra, verilen anahtar için kaydedilmiş tüm takipçiler silinir.
	 *
	 * @param key Takip edilen nesnenin kayıt anahtarı
	 */
	public static void breakMeeting(int key) {
		
		ORGANIZATOR.unFollow(key);
	}
	
	/**
	 * Verilen anahtar ile kayıtlı olan nesneyi döndürür.<br>
	 * Döndürülen nesnenin kaydı devam eder.<br>
	 * Eğer verilen anahtara ait kayıtlı bir nesne yoksa {@code null} döner.
	 *
	 * @param key Nesnenin kayıt anahtarı
	 * @param <T> Nesnenin türü
	 * @return Nesne. Yoksa {@code null}.
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T getObject(int key) {return (T) internet.get(key);}
	
	/**
	 * Verilen anahtar ile nesneyi kaydeder.<br>
	 * Ancak bunun için nesnenin bekleyen bir <u>takipçisi olmamalı</u>.
	 * Eğer bir takipçi varsa, nesne kaydedilmez,
	 * takipçi ile buluşturulur ve olay biter.<br>
	 * Eğer bir takipçi yoksa nesne kaydedilir,
	 * aynı anahtarla kayıtlı bir nesne varsa yeni nesne ile güncellenir.<br>
	 *
	 * @param key Anahtar
	 * @param obj Nesne
	 * @param <T> Nesne türü
	 * @return Kaydedilen nesne
	 */
	@Nullable
	private static <T> T putObject(int key, T obj) {
		
		if (obj != null) {
			
			checkKeepClass(obj);
			
			//Buluşmayı kontrol et
			if (!ORGANIZATOR.meet(key, obj)) {
				
				//! Bu alan, nesnenin bir takipçisi olmadığı anlamına geliyor
				
				//! Bir nesnenin, beklemede olan bir takipçisi varsa tek kullanımlık gibi iş görür,
				//! buluşma sağlanır ve olay biter
				//! Bu biraz karışıklık yaratabilir.
				//! Eğer nesne kalıcı olmalı ise ve nesnenin beklemede olan bir takipçisi varsa kaydedilmiyor.
				//! Bu durumda takipçi, nesne kaydedildikten sonra buluşma talebinde bulunmak zorunda.
				
				//- Buluşma yoksa kaydediliyor
				internet.put(key, obj);
				
				//- Buluşma taleplerinin nesneleri geçici nesneler olarak düşünülmeli
				//- Her nesne kalıcı olarak saklanırsa burası çöplüğe döner
			}
			
			return obj;
		}
		else {
			xlog.d("null object can not be saved");
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
			
			return remove(getKey(obj));
		}
		
		return null;
	}
	
	/**
	 * Verilen sınıf için kaydı siler.
	 *
	 * @param clazz Sınıf
	 * @param <T>   Sınıf türü
	 * @return Kayıtlı nesne, yoksa {@code null}
	 */
	public static <T> T remove(Class<T> clazz) {
		
		if (clazz != null) {
			
			return remove(getKey(clazz));
		}
		
		return null;
	}
	
	/**
	 * Tüm kayıtları siler.
	 */
	public static void clear() {
		
		internet.clear();
		ORGANIZATOR.clear();
		
		xlog.i("All clear");
	}
	
}