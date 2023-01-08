package com.tr.hsyn.xbox.definition;


import com.tr.hsyn.key.Key;
import com.tr.hsyn.registery.cast.Database;
import com.tr.hsyn.xbox.Visitor;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


/**
 * Misafir giriş çıkışlarını kayıt altına alan yazıcı.
 */
public class Writer {
	
	protected final Database<Visitor> register;
	private final   Map<Key, Visitor> dataMap = new HashMap<>();
	
	public Writer(Database<Visitor> database) {
		
		this.register = database;
	}
	
	/**
	 * Çıkış işlemini gerçekleştirir.
	 *
	 * @param key Çıkış yapılan oda
	 * @param t   Çıkış yapan misafir
	 * @param <T> Misafir türü
	 */
	public <T> void remove(@NotNull Key key, @NotNull T t) {
		
		var data = dataMap.get(key);
		data.setExit();
		
		if (register != null) {
			
			
			if (register.update(data)) {
				
				xlog.i("The visitor exited : [%s]", key.getName());
			}
			else {
				
				xlog.i("The visitor exited but could not registered : [%s]", key.getName());
			}
		}
		else {
			
			xlog.i("The visitor exited but no register to register data : [%s]", key.getName());
		}
	}
	
	/**
	 * Misafir ile bir iletişim gerçekleştiğini bildirir.
	 *
	 * @param key Misafirin odası
	 */
	public void interact(@NotNull Key key) {
		
		var data = dataMap.get(key);
		data.interact();
		xlog.i("The data has interacted : [%s]", key.getName());
		
		if (register != null) {
			
			if (register.update(data)) {
				
				xlog.i("Data interaction is registered : [%s]", key.getName());
			}
			else {
				
				xlog.i("Data interaction could not registered : [%s]", key.getName());
			}
		}
		else {
			
			xlog.i("No register to register interaction : [%s]", key.getName());
		}
		
		
	}
	
	/**
	 * Yeni misafir girişini kaydeder.
	 *
	 * @param key Nesne anahtarı
	 * @param t   Aynı odada kalan önceki misafir
	 * @param <T> Misafir nesne türü
	 */
	public <T> void add(@NotNull Key key, @Nullable T t) {
		
		var data = dataMap.get(key);
		
		if (data == null) {
			data = new Visitor(key);
			dataMap.put(key, data);
		}
		else data.setReEnter();
		
		
		if (t != null) {
			
			if (register != null) {
				
				if (register.add(data)) {
					
					xlog.i("The data is registered [%s]", key.getName());
				}
				else {
					
					xlog.i("Data registering is failed [%s]", key.getName());
				}
			}
			else {
				
				xlog.i("No data register to register the data [%s]", data);
			}
			
			xlog.i("Oda yenilendi : [%s] [%s]", key.getName(), t.getClass().getSimpleName());
		}
		else {
			
			xlog.i("Oda tutuldu : [%s]", key.getName());
			
			if (register != null) {
				
				if (register.add(data)) xlog.i("New data is registered : [%s]", key.getName());
				else xlog.i("New could not registered : [%s]", key.getName());
			}
			else {
				
				
				xlog.i("No register to register the new data : [%s]", data);
			}
		}
	}
	
	/**
	 * Yazı işlerini kapatır.
	 */
	public void close() {
		
		if (register != null) {
			register.close();
		}
	}
	
	
}
