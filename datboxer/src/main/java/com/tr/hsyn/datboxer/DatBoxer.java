package com.tr.hsyn.datboxer;


import com.tr.hsyn.datakey.DataKey;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@SuppressWarnings("unchecked")
public class DatBoxer implements Objext {
	
	/**
	 * Object Box.<br>
	 * Alt sınıfların kendine özel bilgiler saklaması için protected kalmalı.
	 */
	protected final Map<DataKey, Object> datamap = new HashMap<>();
	
	public <T> T call(DataKey key, Object... args) {
		
		return null;
	}
	
	@Override
	@Nullable
	public <T> T getData(@NotNull DataKey key) {
		
		if (key.isReadable()) return (T) datamap.get(key);
		else xlog.i("Data is not readable : %s", key);
		
		return null;
	}
	
	@Override
	@Nullable
	public <T> T setData(@NotNull DataKey key, @Nullable T data) {
		
		if (key.isWritable()) {
			
			//- Beklenen durum
			if (data != null) return (T) datamap.put(key, data);
			
			//- Var olan bir veriye null yazılmak isteniyorsa
			if (datamap.get(key) != null) return (T) datamap.remove(key);
		}
		else xlog.i("Data is not writable : %s", key);
		
		return null;
	}
	
	@Override
	public boolean getBool(@NotNull DataKey key) {
		
		if (key.isReadable()) {
			
			Boolean data = (Boolean) datamap.get(key);
			
			return data != null && data;
		}
		
		return false;
	}
	
	@Override
	public boolean getBool(@NotNull DataKey key, boolean defaultValue) {
		
		if (key.isReadable()) {
			
			Boolean data = (Boolean) datamap.get(key);
			
			return data != null && data;
		}
		
		return defaultValue;
	}
	
	@Override
	public int getInt(@NotNull DataKey key, int defaultValue) {
		
		if (key.isReadable()) {
			
			Integer data = (Integer) datamap.get(key);
			
			return data != null ? data : defaultValue;
		}
		
		return defaultValue;
	}
	
	@Override
	public int getInt(@NotNull DataKey key) {
		
		if (key.isReadable()) {
			
			Integer data = (Integer) datamap.get(key);
			
			return data != null ? data : 0;
		}
		
		return 0;
	}
	
	public long getLong(@NotNull DataKey key) {
		
		if (key.isReadable()) {
			
			Long data = (Long) datamap.get(key);
			
			return data != null ? data : 0L;
		}
		
		return 0L;
	}
	
	@Override
	public long getLong(@NotNull DataKey key, long defaultValue) {
		
		if (key.isReadable()) {
			
			Long data = (Long) datamap.get(key);
			
			return data != null ? data : defaultValue;
		}
		
		return defaultValue;
	}
	
	@Override
	@NotNull
	public Set<DataKey> keySet() {
		
		return datamap.keySet().stream().filter(key -> key.isReadable() || key.isWritable()).collect(Collectors.toSet());
	}
	
	@Override
	public boolean exist(@NotNull DataKey key) {
		
		return key.isReadable() && datamap.containsKey(key);
	}
}