package com.tr.hsyn.execution;


import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


/**
 * Bir işi arka planda çalıştırıp, ana thread üzerinde sonucu veren bir yapı sunar.
 * Sadece verilen iş arka planda çalışır, işin sonucu (varsa) veya hata (varsa) ana
 * thread üzerinde işlenir.
 *
 * @param <T>
 */
public interface Work<T> extends WorkType<T> {

	/**
	 * Sonucu başarı ve hata olarak ayrı ayrı almak yerine,
	 * ikisini birsen almayı sağlar.
	 * Ana thread üzerinde çağrılır.
	 * Bu metot diğer iki metodu devre dışı bırakır.
	 * Yani bu metot tanımlanırsa, {@link #onError(Consumer)} ve {@link #onSuccess(Consumer)}
	 * metotları çağrılmaz.
	 *
	 * @param onResult Sonucu işleyecek olan nesne
	 * @return Work
	 */
	Work<T> onResult(BiConsumer<T, Throwable> onResult);

	/**
	 * İşlemin başarısı durumunda ön planda çalışacak olan işi alır.
	 * Ana thread üzerinde.
	 *
	 * @param onSuccess İşleyici
	 * @return Work
	 */
	Work<T> onSuccess(Consumer<T> onSuccess);

	/**
	 * Hata işleyicisini tanımla.
	 * Ana thread üzerinde.
	 *
	 * @param onError Hata işleyicisi
	 * @return Work
	 */
	Work<T> onError(Consumer<Throwable> onError);

	/**
	 * Verilen iş hata ile yada hatasız tamamlandıktan sonra en son çalıştırılmak istenen iş.
	 * Ana thread üzerinde.
	 *
	 * @param onLast İş
	 * @return Work
	 */
	Work<T> onLast(Runnable onLast);

	/**
	 * Verilen iş hata ile yada hatasız tamamlandıktan sonra en son çalıştırılmak istenen iş.
	 * Ana thread üzerinde.
	 *
	 * @param onLast İş
	 * @param delay  Geçikme süresi
	 * @return Work
	 */
	Work<T> onLast(Runnable onLast, long delay);

	/**
	 * Verilen işin çalışmasını sağlar.
	 * Bu çağrı olmazsa hiçbir işlem yapılmaz.
	 */
	void execute();

	/**
	 * İşi gecikmeli başlatır
	 *
	 * @param delay Gecikme süresi
	 */
	void execute(long delay);

	static <T> Work<T> on(Callable<T> callable) {

		return Worker.on(callable);
	}

	static <T> Work<T> on(Runnable runnable) {

		return Worker.on(runnable);
	}


}
