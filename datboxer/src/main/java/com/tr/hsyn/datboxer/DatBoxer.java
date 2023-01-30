package com.tr.hsyn.datboxer;


import com.tr.hsyn.datakey.DataKey;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Veri saklayıcısı.<br>
 * Verileri bir anahtar ile saklar ve aynı anahtar ile erişim sağlar.
 *
 * @see DataKey
 */
@SuppressWarnings("unchecked")
public class DatBoxer {
	
	/**
	 * Object Box
	 */
	private final Map<DataKey, Object> datamap = new HashMap<>();
	
	/**
	 * Veriyi hem saklamayı hem silmeyi sağlar.
	 *
	 * @param key  Anahtar
	 * @param data Veri
	 * @param <T>  Veri türü
	 * @return Eğer veri {@code null} değilse kaydedilir ve aynı yerde saklanan önceki veri döndürülür.
	 * 		Daha önce veri kaydedilmemişse {@code null} döner.<br>
	 * 		Eğer veri {@code null} ise ve verilen anahtarla bir kayıt tutuluyorsa bu kayıt silinir ve silinen kayıt döndürülür.
	 */
	@Nullable
	public <T> T setData(@NotNull DataKey key, T data) {
		
		if (key.isWritable()) {
			
			//- Beklenen durum
			if (data != null)
				return (T) datamap.put(key, data);
			
			//- Var olan bir veriye null yazılmak isteniyorsa
			if (datamap.get(key) != null)
				return (T) datamap.remove(key);
		}
		else xlog.i("Data is not writable : %s", key);
		
		return null;
	}
	
	/**
	 * Bool veriler için {@code null} kontrolü yapar.
	 *
	 * @param key Anahtar
	 * @return Eğer istenen veri yoksa {@code false}, varsa bool veri
	 */
	public boolean getBoolData(@NotNull DataKey key) {
		
		if (key.isReadable()) {
			
			Boolean data = (Boolean) datamap.get(key);
			
			return data != null && data;
		}
		
		return false;
	}
	
	/**
	 * Kayıtlı veriyi döndürür.
	 *
	 * @param key Anahtar
	 * @param <T> Kayıtlı veri türü
	 * @return Veri, yoksa {@code null}
	 */
	@Nullable
	public <T> T getData(@NotNull DataKey key) {
		
		if (key.isReadable()) return (T) datamap.get(key);
		else xlog.i("Data is not readable : %s", key);
		
		return null;
	}
	
	@NotNull
	public Set<DataKey> keySet() {
		
		return datamap.keySet();
	}
	
	public boolean containsKey(DataKey key) {
		
		return datamap.containsKey(key);
	}
}