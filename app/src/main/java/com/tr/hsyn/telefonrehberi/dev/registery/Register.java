package com.tr.hsyn.telefonrehberi.dev.registery;


import android.content.ContentResolver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Kaydedici.<br>
 * <a href="https://github.com/google/gson">Gson</a> kütüphanesini kullanarak
 * nesneleri kaydetmeyi sağlar.
 */
public interface Register {
	
	Gson GSON = new Gson();
	
	/**
	 * Save object.
	 *
	 * @param key    Key
	 * @param object Object
	 * @param <T>    Object type
	 */
	<T> void write(@NonNull String key, @NonNull T object);
	
	/**
	 * Save object list
	 *
	 * @param key        Key
	 * @param objectList List
	 * @param <T>        Object type
	 */
	<T> void write(@NonNull String key, @NonNull List<T> objectList);
	
	/**
	 * Read object
	 *
	 * @param key   Key
	 * @param clazz Object class
	 * @param <T>   Type
	 * @return Object
	 */
	@Nullable
	<T> T read(@NonNull String key, @NonNull Class<T> clazz);
	
	/**
	 * Read object list
	 *
	 * @param key   Key
	 * @param clazz Object class
	 * @param <T>   Class type
	 * @return List of {@code T} or {@code null}
	 */
	@Nullable
	<T> List<T> readList(@NonNull String key, @NonNull Class<T> clazz);
	
	/**
	 * @param key Key
	 * @return {@code true} if the key exist in the room
	 */
	boolean exist(@NonNull String key);
	
	/**
	 * @return All keys in this room
	 */
	@NonNull
	List<String> getKeys();
	
	/**
	 * Delete saved data with the key
	 *
	 * @param key key
	 * @return {@code true} if success
	 */
	boolean delete(@NonNull String key);
	
	/**
	 * Read object.
	 *
	 * @param key          key for object
	 * @param clazz        object class
	 * @param defaultValue if object not found
	 * @param <T>          object type
	 * @return object or defaultValue
	 */
	default <T> T read(@NonNull String key, @NonNull Class<T> clazz, T defaultValue) {
		
		T t = read(key, clazz);
		
		return t != null ? t : defaultValue;
	}
	
	/**
	 * Read object list.
	 *
	 * @param key          key for object
	 * @param clazz        object class
	 * @param defaultValue if object not found
	 * @param <T>          object type
	 * @return object list or default value
	 */
	default <T> List<T> readList(@NonNull String key, @NonNull Class<T> clazz, List<T> defaultValue) {
		
		List<T> t = readList(key, clazz);
		
		return t != null ? t : defaultValue;
	}
	
	@NonNull
	static <T> Type getTypeOfList(@NonNull Class<T> clazz) {
		
		return TypeToken.getParameterized(List.class, clazz).getType();
	}
	
	@NonNull
	static <T> String toJson(@NonNull T object) {
		
		return GSON.toJson(object);
	}
	
	static <T> T objectFromJson(@NonNull String json, Class<T> clazzType) {
		
		return GSON.fromJson(json, clazzType);
	}
	
	static <T> List<T> objectListFromJson(@NonNull String json, Class<T> clazzType) {
		
		return GSON.fromJson(json, getTypeOfList(clazzType));
	}
	
	/**
	 * Yeni bir kaydedici döndürür.
	 * Kayıtların tutulacağı dizin, uygulamanın kullanıcıdan aldığı ana dizin olacak ve
	 * dosya oluşturma silme sorgulama işlemlerinin hepsi bu dizin altında gerçekleşecek.
	 * Bu dizin bu yüzden daha önce kullanıcıdan alınmış olmalı, aksi halde yapılacak herhangi bir işlem
	 * hata ile sonuçlanacaktır.
	 *
	 * @return Register
	 */
	@NonNull
	static Register openDocument() {
		
		return new DocumentRegister();
	}
	
	/**
	 * Yeni bir kaydedici döndürür.
	 * Kayıtların tutulacağı dizin, uygulamanın kullanıcıdan aldığı ana dizin olacak ve
	 * dosya oluşturma silme sorgulama işlemlerinin hepsi bu dizin altında gerçekleşecek.
	 * Bu dizin bu yüzden daha önce kullanıcıdan alınmış olmalı, aksi halde yapılacak herhangi bir işlem
	 * hata ile sonuçlanacaktır.
	 *
	 * @param directoryName ana dizin altındaki ilgili dizinin adı
	 * @return Register
	 */
	@NonNull
	static Register openDocument(@NonNull String directoryName) {
		
		return new DocumentRegister(directoryName);
	}
	
	/**
	 * Yeni bir kaydedici döndürür.
	 * Kayıtların tutulacağı dizin, uygulamanın kullanıcıdan aldığı ana dizin olacak ve
	 * dosya oluşturma silme sorgulama işlemlerinin hepsi bu dizin altında gerçekleşecek.
	 * Bu dizin bu yüzden kullanıcıdan alınmış olmalı, aksi halde yapılacak herhangi bir işlem
	 * hata ile sonuçlanacaktır.
	 *
	 * @param resolver ContentResolver
	 * @return Register
	 */
	@NonNull
	static Register openDocument(@NonNull ContentResolver resolver) {
		
		return new DocumentRegister(resolver);
	}
	
	/**
	 * Yeni bir kaydedici döndürür.
	 * Kayıtların tutulacağı dizin, uygulamanın kullanıcıdan aldığı ana dizin olacak ve
	 * dosya oluşturma silme sorgulama işlemlerinin hepsi bu dizin altında
	 * altında oluşturulacak bir dizin içinde gerçekleşecek.
	 * Bu dizin bu yüzden kullanıcıdan alınmış olmalı, aksi halde yapılacak herhangi bir işlem
	 * hata ile sonuçlanacaktır.
	 *
	 * @param resolver      ContentResolver
	 * @param directoryName Ana dizinin altındaki ilgili dizinin adı
	 * @return Register
	 */
	@NonNull
	static Register openDocument(@NonNull ContentResolver resolver, @NonNull String directoryName) {
		
		return new DocumentRegister(resolver, directoryName);
	}
	
	/**
	 * Yeni bir kaydedici döndürür.
	 * Verilen dizin, yapılacak tüm kayıtların ana dizini olacak.
	 * Yani dosya oluşturma silme sorgulama işlemlerinin hepsi bu dizinin içinde gerçekleşecek.
	 *
	 * @param resolver ContentResolver
	 * @param room     kayıtların tutulacağı dizin
	 * @return Register
	 */
	@NonNull
	static Register openDocument(@NonNull ContentResolver resolver, @NonNull DocumentFile room) {
		
		return new DocumentRegister(resolver, room);
	}
	
	/**
	 * Yeni bir kaydedici döndürür.<br>
	 * Verilen dizin adı, verilen doküman dizini altında yeni bir dizin oluşturur (yoksa).
	 * Ve tüm işlemler bu dizin altında gerçekleşir.
	 *
	 * @param resolver      ContentResolver
	 * @param room          ana dizin
	 * @param directoryName ana dizin altındaki dizin adı
	 * @return Register
	 */
	@NonNull
	static Register openDocument(@NonNull ContentResolver resolver, @NonNull DocumentFile room, @NonNull String directoryName) {
		
		return new DocumentRegister(resolver, room, directoryName);
	}
	
	/**
	 * Kayıt işlemleri uygulamanın kendi dizininde gerçekleşir.
	 * Bu kayıtlar uygulama kaldırıldığında silinir.
	 *
	 * @return Register
	 */
	@NonNull
	static Register openFile() {
		
		return new FileRegister();
	}
	
	/**
	 * Kayıt işlemleri uygulamanın kendi dizininde gerçekleşir.
	 * Bu kayıtlar uygulama kaldırıldığında silinir.
	 *
	 * @param directoryName alt dizin adı
	 * @return Register
	 */
	@NonNull
	static Register openFile(@NonNull String directoryName) {
		
		return new FileRegister(directoryName);
	}
}
