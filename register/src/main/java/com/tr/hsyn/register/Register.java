package com.tr.hsyn.register;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@SuppressWarnings("UnusedReturnValue")
@SuppressLint("ApplySharedPref")
public class Register {
	
	private final SharedPreferences        mSharedPreferences;
	private final SharedPreferences.Editor editor;
	private final Gson                     gson;
	
	@SuppressLint("CommitPrefEdits")
	public Register(@NonNull final Context context, @NonNull final String prefName) {
		
		mSharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		editor             = mSharedPreferences.edit();
		gson               = new Gson();
	}
	
	public Register(@NonNull final Object objectFromContext, @NonNull String name) {
		
		if (!(objectFromContext instanceof Context context)) {
			
			throw new IllegalArgumentException("Object must have context : " + objectFromContext.getClass().getCanonicalName());
		}
		
		mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		editor             = mSharedPreferences.edit();
		gson               = new Gson();
	}
	
	public void apply() {
		
		editor.apply();
	}
	
	public void commit() {
		
		editor.commit();
	}
	
	public SharedPreferences.Editor putInt(String key, int value) {
		
		editor.putInt(key, value);
		return editor;
	}
	
	public int getInt(String key, int defaultValue) {
		
		return mSharedPreferences.getInt(key, defaultValue);
	}
	
	public SharedPreferences.Editor putBoolean(String key, boolean value) {
		
		editor.putBoolean(key, value);
		return editor;
	}
	
	public Register remove(String key) {
		
		editor.remove(key);
		return this;
	}
	
	public Register clear() {
		
		editor.clear();
		return this;
	}
	
	public boolean getBoolean(String key, boolean defaultValue) {
		
		return mSharedPreferences.getBoolean(key, defaultValue);
	}
	
	public boolean contains(String key) {
		
		return mSharedPreferences.contains(key);
	}
	
	public SharedPreferences.Editor putFloat(String key, float value) {
		
		editor.putFloat(key, value);
		return editor;
	}
	
	public float getFloat(String key, float defaultValue) {
		
		return mSharedPreferences.getFloat(key, defaultValue);
	}
	
	public SharedPreferences.Editor putLong(String key, long value) {
		
		editor.putLong(key, value);
		return editor;
	}
	
	public long getLong(String key, long defaultValue) {
		
		return mSharedPreferences.getLong(key, defaultValue);
	}
	
	public SharedPreferences.Editor putString(String key, String value) {
		
		editor.putString(key, value);
		return editor;
	}
	
	public SharedPreferences.Editor putStringSet(String key, Set<String> values) {
		
		editor.putStringSet(key, values);
		return editor;
	}
	
	public String getString(String key, String defaultValue) {
		
		return mSharedPreferences.getString(key, defaultValue);
	}
	
	@Nullable
	public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
		
		return mSharedPreferences.getStringSet(key, defValues);
	}
	
	public <T> SharedPreferences.Editor putObject(String key, T object) {
		
		String objectString = gson.toJson(object);
		editor.putString(key, objectString);
		return editor;
	}
	
	public <T> T getObject(String key, Class<T> classType) {
		
		String objectString = mSharedPreferences.getString(key, null);
		
		if (objectString != null) {
			
			return gson.fromJson(objectString, classType);
		}
		
		return null;
	}
	
	public <T> SharedPreferences.Editor putObjectsList(String key, List<T> objectList) {
		
		String objectString = gson.toJson(objectList);
		editor.putString(key, objectString);
		return editor;
	}
	
	@NonNull
	private <T> Type getType(@NonNull Class<T> clazz) {
		
		return TypeToken.getParameterized(List.class, clazz).getType();
	}
	
	public <T> List<T> getObjectsList(String key, Class<T> itemClassType) {
		
		String objectString = mSharedPreferences.getString(key, null);
		
		if (objectString != null) {
			
			return gson.fromJson(objectString, getType(itemClassType));
		}
		
		return new ArrayList<>();
	}
	
	public void clearSession() {
		
		editor.clear();
		editor.commit();
	}
	
	public boolean deleteValue(String key) {
		
		if (isKeyExists(key)) {
			
			editor.remove(key);
			editor.commit();
			return true;
		}
		
		return false;
	}
	
	public boolean isKeyExists(String key) {
		
		Map<String, ?> map = mSharedPreferences.getAll();
		return map.containsKey(key);
	}
	
	public SharedPreferences.Editor edit() {
		
		return editor;
	}
	
	public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
		
		mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
	}
	
	public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
		
		mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
	}
	
	public Set<String> keys() {
		
		return mSharedPreferences.getAll().keySet();
	}
	
	public Map<String, ?> getAll() {
		
		return mSharedPreferences.getAll();
	}
	
	public static Register on(@NonNull final Context context, @NonNull String prefName) {
		
		return new Register(context, prefName);
	}
}
