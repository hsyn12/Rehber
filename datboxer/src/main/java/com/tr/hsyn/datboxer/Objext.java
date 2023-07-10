package com.tr.hsyn.datboxer;


import com.tr.hsyn.datakey.DataKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;


/**
 * An advanced object that can hold data and can be accessed by key.
 * The accessing the data by {@link DataKey} and under control.
 *
 * @see DataKey
 */
public interface Objext {
	
	/**
	 * Returns the data for the given key.
	 * To be able to read the data, the key must be readable.
	 *
	 * @param key key of the data
	 * @param <T> data type
	 * @return the data
	 */
	<T> T getData(@NotNull DataKey key);
	
	/**
	 * Adds or remove data for the given key.
	 * To be able to write the data, the key must be writable.
	 *
	 * @param key  key of the data
	 * @param data data to be added or removed
	 * @param <T>  data type
	 * @return If the given data is not null, it is stored and returns the earlier data.
	 * 		If the given data is null, and exists the data for the given key,
	 * 		it is removed and returns the removed data.
	 */
	<T> T setData(@NotNull DataKey key, @Nullable T data);
	
	/**
	 * Returns the boolean value of the given key.
	 * To be able to read the data, the key must be readable.
	 *
	 * @param key key of the data
	 * @return {@code false} if the data is null or data value.
	 */
	boolean getBool(@NotNull DataKey key);
	
	/**
	 * Returns the boolean value of the given key.
	 * To be able to read the data, the key must be readable.
	 *
	 * @param key          key of the data
	 * @param defaultValue default value if the data is null
	 * @return default value if the data is null or data value
	 */
	boolean getBool(@NotNull DataKey key, boolean defaultValue);
	
	/**
	 * Returns the int value of the given key.
	 * To be able to read the data, the key must be readable.
	 *
	 * @param key          key of the data
	 * @param defaultValue default value if the data is null
	 * @return default value if the data is null or data value
	 */
	int getInt(@NotNull DataKey key, int defaultValue);
	
	/**
	 * Returns the long value of the given key.
	 * To be able to read the data, the key must be readable.
	 *
	 * @param key key of the data
	 * @return default value if the data is null or data value. The default value is {@code 0L}.
	 */
	int getInt(@NotNull DataKey key);
	
	/**
	 * Returns the long value of the given key.
	 * To be able to read the data, the key must be readable.
	 *
	 * @param key Key
	 * @return Long or {@code 0L}
	 */
	long getLong(@NotNull DataKey key);
	
	/**
	 * Returns the long value of the given key.
	 * To be able to read the data, the key must be readable.
	 *
	 * @param key          key of the data
	 * @param defaultValue default value if the data is null
	 * @return default value if the data is null or data value
	 */
	long getLong(@NotNull DataKey key, long defaultValue);
	
	/**
	 * @return All readable and writable keys.
	 */
	Set<DataKey> keySet();
	
	/**
	 * Checks if the given key exists.
	 *
	 * @param key key
	 * @return {@code true} if the key is readable and exists.
	 */
	boolean exist(@NotNull DataKey key);
	
	
}
