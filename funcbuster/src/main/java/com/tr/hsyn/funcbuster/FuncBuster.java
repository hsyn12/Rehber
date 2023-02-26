package com.tr.hsyn.funcbuster;


import com.tr.hsyn.datakey.DataKey;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * Basit ve en temel fonksiyonları (fonksiyon nesnelerini (Functional Interfaces)) saklar ve erişim sağlar.
 */
@SuppressWarnings("unchecked")
public class FuncBuster {
	
	protected final Map<DataKey, Object> funcMap = new HashMap<>();
	
	/**
	 * İstenen fonksiyon nesnesini döndürür.
	 *
	 * @param key Nesnenin kayıtlı olduğu anahtar
	 * @param <P> Fonksiyonun parametre türü
	 * @param <R> Fonksiyonun dönüş türü
	 * @return Verilen anahtarla kayıtlı {@link Function}, yoksa {@code null}
	 */
	@Nullable
	public <P, R> Function<P, R> getFunction(@NotNull DataKey key) {
		
		if (key.isReadable()) return (Function<P, R>) funcMap.get(key);
		else xlog.i("Data is not readable : %s", key);
		
		return null;
	}
	
	/**
	 * Parametre almayan, değer döndüren fonksiyon nesnesini döndürür.
	 *
	 * @param key Nesnenin kayıtlı olduğu anahtar
	 * @param <R> Fonksiyonun dönüş türü
	 * @return Verilen anahtarla kayıtlı {@link Supplier}, yoksa {@code null}
	 */
	@Nullable
	public <R> Supplier<R> getSupplier(@NotNull DataKey key) {
		
		if (key.isReadable()) return (Supplier<R>) funcMap.get(key);
		else xlog.i("Data is not readable : %s", key);
		
		return null;
	}
	
	/**
	 * Parametre alan, değer döndürmeyen fonksiyon nesnesini döndürür.
	 *
	 * @param key Nesnenin kayıtlı olduğu anahtar
	 * @param <P> Fonksiyonun parametre türü
	 * @return Verilen anahtarla kayıtlı {@link Consumer}, yoksa {@code null}
	 */
	@Nullable
	public <P> Consumer<P> getConsumer(@NotNull DataKey key) {
		
		if (key.isReadable()) return (Consumer<P>) funcMap.get(key);
		else xlog.i("Data is not readable : %s", key);
		
		return null;
	}
	
	/**
	 * Parametre almayan ve değer döndürmeyen fonksiyon nesnesini döndürür.
	 *
	 * @param key Nesnenin kayıtlı olduğu anahtar
	 * @return Verilen anahtarla kayıtlı {@link Runnable}, yoksa {@code null}
	 */
	@Nullable
	public Runnable getRunnable(@NotNull DataKey key) {
		
		if (key.isReadable()) return (Runnable) funcMap.get(key);
		else xlog.i("Data is not readable : %s", key);
		
		return null;
	}
	
	/**
	 * Fonksiyon nesnesinin ilgili metodunu çağırır.
	 *
	 * @param key Nesnenin kayıtlı olduğu anahtar
	 */
	public void callRunnable(@NotNull DataKey key) {
		
		if (key.isReadable()) {
			
			Runnable func = getRunnable(key);
			
			if (func != null) func.run();
		}
		else xlog.i("Data is not writable : %s", key);
	}
	
	/**
	 * Fonksiyon nesnesinin ilgili metodunu çağırır.
	 *
	 * @param key Nesnenin kayıtlı olduğu anahtar
	 * @param <R> Dönüş türü
	 * @return Çağrıdan elde edilen nesne
	 */
	@Nullable
	public <R> R callSupplier(@NotNull DataKey key) {
		
		if (key.isReadable()) {
			
			Supplier<R> func = getSupplier(key);
			
			if (func != null) return func.get();
		}
		else xlog.i("Data is not writable : %s", key);
		
		return null;
	}
	
	/**
	 * Fonksiyon nesnesinin ilgili metodunu çağırır.
	 *
	 * @param key Nesnenin kayıtlı olduğu anahtar
	 * @param p   Parametre değişkeni
	 * @param <P> Parametre türü
	 */
	public <P> void callConsumer(@NotNull DataKey key, @Nullable P p) {
		
		if (key.isReadable()) {
			
			Consumer<P> func = getConsumer(key);
			if (func != null) func.accept(p);
		}
		else xlog.i("Data is not writable : %s", key);
	}
	
	/**
	 * Fonksiyon nesnesinin ilgili metodunu çağırır.
	 *
	 * @param key Nesnenin kayıtlı olduğu anahtar
	 * @param p   Parametre değişkeni
	 * @param <P> Parametre türü
	 * @param <R> Dönüş türü
	 * @return Çağrılan metodun döndürdüğü nesne
	 */
	@Nullable
	public <P, R> R callFunction(@NotNull DataKey key, @Nullable P p) {
		
		if (key.isReadable()) {
			
			Function<P, R> func = getFunction(key);
			
			if (func != null) return func.apply(p);
		}
		else xlog.i("Data is not writable : %s", key);
		
		return null;
	}
	
	/**
	 * Fonksiyonu kaydeder veya siler.
	 *
	 * @param key  Nesnenin kayıt edileceği anahtar
	 * @param func Fonksiyon nesnesi veya {@code null} (kayıtlı nesneyi silmek için)
	 * @param <P>  Parametre türü
	 * @param <R>  Dönüş türü
	 * @return Kaydedilmek istenen fonksiyon {@code null} değilse kaydedilir ve önceki kayıtlı nesne döndürülür.
	 * 		Kaydedilmek istenen fonksiyon {@code null} ise ve verilen anahtarla kayıtlı bir nesne varsa bu nesne silinir ve silinen nesne döner, kayıtlı nesne yoksa {@code null}
	 */
	@Nullable
	public <P, R> Function<P, R> setFunction(@NotNull DataKey key, @Nullable Function<P, R> func) {
		
		if (key.isWritable()) {
			
			if (func != null) return (Function<P, R>) funcMap.put(key, func);
			if (getFunction(key) != null) return (Function<P, R>) funcMap.remove(key);
		}
		else xlog.i("Data is not writable : %s", key);
		
		return null;
	}
	
	/**
	 * Verilen fonksiyon nesnesini kaydeder veya siler.
	 *
	 * @param key  Nesnenin kayıt edileceği anahtar
	 * @param func Fonksiyon nesnesi veya {@code null} (kayıtlı nesneyi silmek için)
	 * @param <R>  Dönüş türü
	 * @return Kaydedilmek istenen fonksiyon {@code null} değilse kaydedilir ve önceki kayıtlı nesne döndürülür.
	 * 		Kaydedilmek istenen fonksiyon {@code null} ise ve verilen anahtarla kayıtlı bir nesne varsa bu nesne silinir vesilinen nesne döner, kayıtlı nesne yoksa {@code null}
	 */
	@Nullable
	public <R> Supplier<R> setSupplier(@NotNull DataKey key, @Nullable Supplier<R> func) {
		
		if (key.isWritable()) {
			
			if (func != null) return (Supplier<R>) funcMap.put(key, func);
			if (getFunction(key) != null) return (Supplier<R>) funcMap.remove(key);
		}
		else xlog.i("Data is not writable : %s", key);
		
		return null;
	}
	
	/**
	 * Verilen fonksiyon nesnesini kaydeder veya siler.
	 *
	 * @param key  Nesnenin kayıt edileceği anahtar
	 * @param func Fonksiyon nesnesi veya {@code null} (kayıtlı nesneyi silmek için)
	 * @param <P>  Parametre türü
	 * @return Kaydedilmek istenen fonksiyon {@code null} değilse kaydedilir ve önceki kayıtlı nesne döndürülür.
	 * 		Kaydedilmek istenen fonksiyon {@code null} ise ve verilen anahtarla kayıtlı bir nesne varsa bu nesne silinir ve silinen nesne döner, kayıtlı nesne yoksa {@code null}
	 */
	@Nullable
	public <P> Consumer<P> setConsumer(@NotNull DataKey key, @Nullable Consumer<P> func) {
		
		if (key.isWritable()) {
			
			if (func != null) return (Consumer<P>) funcMap.put(key, func);
			if (getFunction(key) != null) return (Consumer<P>) funcMap.remove(key);
		}
		else xlog.i("Data is not writable : %s", key);
		
		return null;
	}
	
	/**
	 * Verilen fonksiyon nesnesini kaydeder veya siler.
	 *
	 * @param key  Nesnenin kayıt edileceği anahtar
	 * @param func Fonksiyon nesnesi veya {@code null} (kayıtlı nesneyi silmek için)
	 * @return Kaydedilmek istenen fonksiyon {@code null} değilse kaydedilir ve önceki kayıtlı nesne döndürülür (Kayıt yoksa {@code null}).
	 * 		Kaydedilmek istenen fonksiyon {@code null} ise ve verilen anahtarla kayıtlı bir nesne varsa bu nesne silinir ve silinen nesne döner, kayıtlı nesne yoksa {@code null}
	 */
	@Nullable
	public Runnable setRunnable(@NotNull DataKey key, @Nullable Runnable func) {
		
		if (key.isWritable()) {
			
			if (func != null) return (Runnable) funcMap.put(key, func);
			if (getFunction(key) != null) return (Runnable) funcMap.remove(key);
		}
		else xlog.i("Data is not writable : %s", key);
		
		return null;
	}
}