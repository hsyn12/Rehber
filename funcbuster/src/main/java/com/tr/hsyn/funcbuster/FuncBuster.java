package com.tr.hsyn.funcbuster;


import com.tr.hsyn.datakey.DataKey;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * Definition of the <strong>function holder</strong>.<br>
 * The classes that implement this interface can hold and use function objects.<br>
 * The stored functions are registered with {@link DataKey}.
 * In this way, all accesses to the functions are safe and under control.
 *
 * @see DataKey
 */
@SuppressWarnings("unchecked")
public interface FuncBuster {
	
	/**
	 * The {@link Map} object that holds the function objects.
	 *
	 * @return The {@link Map} object to hold the functions
	 */
	@NotNull
	Map<DataKey, Object> getFuncMap();
	
	/**
	 * Returns the function object for the given key.
	 *
	 * @param key data key of the function
	 * @param <P> type of the parameter
	 * @param <R> type of the return value
	 * @return the function object or {@code null} if the key is not readable or does not exist.
	 */
	@Nullable
	default <P, R> Function<P, R> getFunction(@NotNull DataKey key) {
		
		if (key.isReadable()) return (Function<P, R>) getFuncMap().get(key);
		else xlog.i("Data is not readable : %s", key);
		
		return null;
	}
	
	/**
	 * Returns the supplier function object for the given key.
	 *
	 * @param key data key of the function
	 * @param <R> type of the return value
	 * @return the supplier function object or {@code null} if the key is not readable or does not exist.
	 */
	@Nullable
	default <R> Supplier<R> getSupplier(@NotNull DataKey key) {
		
		if (key.isReadable()) return (Supplier<R>) getFuncMap().get(key);
		else xlog.i("Data is not readable : %s", key);
		
		return null;
	}
	
	/**
	 * Returns the consumer function object for the given key.
	 *
	 * @param key data key of the function
	 * @param <P> type of the parameter for the consumer function
	 * @return the consumer function object or {@code null} if the key is not readable or does not exist.
	 */
	@Nullable
	default <P> Consumer<P> getConsumer(@NotNull DataKey key) {
		
		if (key.isReadable()) return (Consumer<P>) getFuncMap().get(key);
		else xlog.i("Data is not readable : %s", key);
		
		return null;
	}
	
	/**
	 * Returns the runnable function object for the given key.
	 *
	 * @param key data key of the function
	 * @return the runnable function object or {@code null} if the key is not readable or does not exist.
	 */
	@Nullable
	default Runnable getRunnable(@NotNull DataKey key) {
		
		if (key.isReadable()) return (Runnable) getFuncMap().get(key);
		else xlog.i("Data is not readable : %s", key);
		
		return null;
	}
	
	/**
	 * Calls the runnable function object for the given key.
	 *
	 * @param key data key of the function
	 */
	default void callRunnable(@NotNull DataKey key) {
		
		if (key.isReadable()) {
			
			Runnable func = getRunnable(key);
			
			if (func != null) func.run();
		}
		else xlog.i("Data is not readable : %s", key);
	}
	
	/**
	 * Calls the supplier function object for the given key.
	 *
	 * @param key data key of the function
	 * @param <R> type of the return value
	 * @return the returned value of the function object or {@code null} if the key is not readable
	 * 		or does not exist.
	 */
	@Nullable
	default <R> R callSupplier(@NotNull DataKey key) {
		
		if (key.isReadable()) {
			
			Supplier<R> func = getSupplier(key);
			
			if (func != null) return func.get();
		}
		else xlog.i("Data is not readable : %s", key);
		
		return null;
	}
	
	/**
	 * Calls the consumer function object for the given key.
	 *
	 * @param key data key of the function
	 * @param p   the parameter
	 * @param <P> type of the parameter
	 */
	default <P> void callConsumer(@NotNull DataKey key, @Nullable P p) {
		
		if (key.isReadable()) {
			
			Consumer<P> func = getConsumer(key);
			if (func != null) func.accept(p);
		}
		else xlog.i("Data is not readable : %s", key);
	}
	
	/**
	 * Calls the function object for the given key.
	 *
	 * @param key data key of the function
	 * @param p   the parameter
	 * @param <P> type of the parameter
	 * @param <R> type of the return value
	 * @return the returned value of the function object or {@code null} if the key is not readable
	 * 		or does not exist.
	 */
	@Nullable
	default <P, R> R callFunction(@NotNull DataKey key, @Nullable P p) {
		
		if (key.isReadable()) {
			
			Function<P, R> func = getFunction(key);
			
			if (func != null) return func.apply(p);
		}
		else xlog.i("Data is not readable : %s", key);
		
		return null;
	}
	
	/**
	 * Stores or remove the function object for the given key.
	 *
	 * @param key  data key of the function
	 * @param func function object to store or {@code null} to remove the existing function
	 * @param <P>  type of the parameter
	 * @param <R>  type of the return value
	 * @return if the function to be registered is not {@code null},
	 * 		it is saved,
	 * 		and returns the earlier registered object or {@code null} if the key does not exist.
	 * 		If the function to be registered is {@code null}
	 * 		and there is an object registered with the given key,
	 * 		that object is deleted,
	 * 		and the deleted object is returned,
	 * 		if there is no registered object then returns {@code null}
	 */
	@Nullable
	default <P, R> Function<P, R> setFunction(@NotNull DataKey key, @Nullable Function<P, R> func) {
		
		if (key.isWritable()) {
			
			if (func != null) return (Function<P, R>) getFuncMap().put(key, func);
			if (getFunction(key) != null) return (Function<P, R>) getFuncMap().remove(key);
		}
		else xlog.i("Data is not writable : %s", key);
		
		return null;
	}
	
	/**
	 * Stores or remove the supplier function object for the given key.
	 *
	 * @param key  data key of the function
	 * @param func function object to store or {@code null} to remove the existing function
	 * @param <R>  type of the return value
	 * @return if the function to be registered is not {@code null},
	 * 		it is saved,
	 * 		and returns the earlier registered object or {@code null} if the key does not exist.
	 * 		If the function to be registered is {@code null}
	 * 		and there is an object registered with the given key,
	 * 		that object is deleted,
	 * 		and the deleted object is returned,
	 * 		if there is no registered object then returns {@code null}
	 */
	@Nullable
	default <R> Supplier<R> setSupplier(@NotNull DataKey key, @Nullable Supplier<R> func) {
		
		if (key.isWritable()) {
			
			if (func != null) return (Supplier<R>) getFuncMap().put(key, func);
			if (getFunction(key) != null) return (Supplier<R>) getFuncMap().remove(key);
		}
		else xlog.i("Data is not writable : %s", key);
		
		return null;
	}
	
	/**
	 * Stores or remove the consumer function object for the given key.
	 *
	 * @param key  data key of the function
	 * @param func function object to store or {@code null} to remove the existing function
	 * @param <P>  type of the parameter
	 * @return if the function to be registered is not {@code null},
	 * 		it is saved,
	 * 		and returns the earlier registered object or {@code null} if the key does not exist.
	 * 		If the function to be registered is {@code null}
	 * 		and there is an object registered with the given key,
	 * 		that object is deleted,
	 * 		and the deleted object is returned,
	 * 		if there is no registered object then returns {@code null}
	 */
	@Nullable
	default <P> Consumer<P> setConsumer(@NotNull DataKey key, @Nullable Consumer<P> func) {
		
		if (key.isWritable()) {
			
			if (func != null) return (Consumer<P>) getFuncMap().put(key, func);
			if (getFunction(key) != null) return (Consumer<P>) getFuncMap().remove(key);
		}
		else xlog.i("Data is not writable : %s", key);
		
		return null;
	}
	
	/**
	 * Stores or remove the runnable function object for the given key.
	 *
	 * @param key  data key of the function
	 * @param func function object to store or {@code null} to remove the existing function
	 * @return if the function to be registered is not {@code null},
	 * 		it is saved,
	 * 		and returns the earlier registered object or {@code null} if the key does not exist.
	 * 		If the function to be registered is {@code null}
	 * 		and there is an object registered with the given key,
	 * 		that object is deleted,
	 * 		and the deleted object is returned,
	 * 		if there is no registered object then returns {@code null}
	 */
	@Nullable
	default Runnable setRunnable(@NotNull DataKey key, @Nullable Runnable func) {
		
		if (key.isWritable()) {
			
			if (func != null) return (Runnable) getFuncMap().put(key, func);
			if (getFunction(key) != null) return (Runnable) getFuncMap().remove(key);
		}
		else xlog.i("Data is not writable : %s", key);
		
		return null;
	}
}