package com.tr.hsyn.xbox;


import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.xbox.definition.Writer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


/**
 * <h2>Daniel</h2>
 * <p>
 * 21 yaşında, genç ve idealist bir öğrenci.
 * Yakışıklı olduğu kadar küstah da.
 * Ancak işinin ehli bir resepsiyonist.
 * Misafirlerin otele giriş çıkışlarını sağlar ve bunun kayıt altına alınmasını sağlar.
 *
 * @author hsyn 5 Ocak 2023 Perşembe 19:45
 */
@SuppressWarnings("unchecked")
public class Daniel extends Reception {
	
	private final Map<Key, Object> OBJECT_MAP = new HashMap<>();
	
	public Daniel(@NotNull Writer writer) {
		
		super(writer);
	}
	
	/**
	 * Odadaki nesneyi döndürür.
	 *
	 * @param key Oda anahtarı
	 * @param <T> Nesne türü
	 * @return Nesne
	 */
	@Override
	public <T> @Nullable T place(@NotNull Key key) {
		
		T value = (T) OBJECT_MAP.get(key);
		
		if (value != null) {
			
			Runny.run(() -> writer.interact(key), false);
		}
		
		return value;
	}
	
	/**
	 * Odaya yeni nesneyi yerleştirir.
	 *
	 * @param key    Oda anahtarı
	 * @param object Yerleştirilecek nesne
	 * @param <T>    Nesne türü
	 * @return Odadaki önceki nesne, yoksa {@code null}
	 */
	@Override
	public <T> @Nullable T place(@NotNull Key key, @NotNull T object) {
		
		T t = (T) OBJECT_MAP.put(key, object);
		//add(key, t);
		Runny.run(() -> writer.add(key, t), false);
		
		return t;
	}
	
	@Override
	public <T> T exit(@NotNull Key key) {
		
		T t = (T) OBJECT_MAP.remove(key);
		
		if (t != null) {
			
			Runny.run(() -> writer.remove(key, t), false);
		}
		
		return t;
	}
	
	@Override
	public void close() {
		
		writer.close();
		OBJECT_MAP.clear();
	}
	
}
