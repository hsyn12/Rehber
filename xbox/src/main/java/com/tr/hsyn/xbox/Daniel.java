package com.tr.hsyn.xbox;


import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.xbox.definition.Reception;
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
 * Misafirlerin otele giriş çıkışlarını sağlar.
 *
 * @author hsyn 5 Ocak 2023 Perşembe 19:45
 */
@SuppressWarnings("unchecked")
public class Daniel extends Reception {
	
	private final Map<Key, Object> OBJECT_MAP = new HashMap<>();
	
	public Daniel(@NotNull Writer writer) {
		
		super(writer);
	}
	
	@Override
	public <T> @Nullable T place(@NotNull Key key) {
		
		T value = (T) OBJECT_MAP.get(key);
		
		if (value != null) {
			
			Runny.run(() -> writer.interact(key), false);
		}
		
		return value;
	}
	
	@Override
	public <T> @Nullable T place(@NotNull Key key, @NotNull T object) {
		
		T t = (T) OBJECT_MAP.put(key, object);
		
		Runny.run(() -> writer.add(key), false);
		
		return t;
	}
	
	@Override
	public boolean exist(@NotNull Key key) {
		
		return OBJECT_MAP.containsKey(key);
	}
	
	@Override
	public <T> T exit(@NotNull Key key) {
		
		T t = (T) OBJECT_MAP.remove(key);
		
		if (t != null) {
			
			Runny.run(() -> writer.remove(key), false);
		}
		
		return t;
	}
	
	@Override
	public void close() {
		
		writer.close();
		OBJECT_MAP.clear();
	}
	
}
