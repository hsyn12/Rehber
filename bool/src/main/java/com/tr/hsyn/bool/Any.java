package com.tr.hsyn.bool;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;


/**
 * <h1>Any</h1>
 * <p>
 * Provides more functional approach to the nullable objects.
 *
 * @param <T>
 * @author hsyn 13 Haziran 2022 Pazartesi 21:05
 */
@FunctionalInterface
public interface Any<T> {
	
	/**
	 * The held object.
	 *
	 * @return object
	 */
	@Nullable T getObject();
	
	/**
	 * @return {@code true} if the object is null.
	 */
	default boolean isNull() {
		
		return getObject() == null;
	}
	
	/**
	 * @return {@code true} if the object is not null.
	 */
	default boolean isNotNull() {
		
		return getObject() != null;
	}
	
	/**
	 * Calls the runnable if the object is null.
	 *
	 * @param runnable runnable
	 * @return this object
	 */
	default Any<T> ifNull(Runnable runnable) {
		
		if (isNull()) runnable.run();
		return this;
	}
	
	/**
	 * Calls the runnable if the object is not null.
	 *
	 * @param runnable runnable
	 * @return this object
	 */
	default Any<T> ifNotNull(Runnable runnable) {
		
		if (isNotNull()) runnable.run();
		return this;
	}
	
	/**
	 * Calls the consumer if the object is not null.
	 *
	 * @param consumer consumer
	 * @return this object
	 */
	default Any<T> ifNotNull(Consumer<T> consumer) {
		
		if (isNotNull()) consumer.accept(getObject());
		return this;
	}
	
	default boolean equalTo(Object o) {
		
		return o instanceof Any && Objects.equals(getObject(), ((Any<?>) o).getObject());
	}
	
	@NotNull
	static <T> Any<T> of(T value) {
		
		return () -> value;
	}
}
