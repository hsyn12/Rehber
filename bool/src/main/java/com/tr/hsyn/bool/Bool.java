package com.tr.hsyn.bool;


import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class Bool implements Any<Boolean> {
	
	public static final     Bool    FALSE = new Bool();
	public static final     Bool    TRUE  = new Bool(true);
	public static final     Bool    NONE  = new Bool(null);
	@Nullable private final Boolean value;
	
	/**
	 * Creates a new Bool instance.
	 */
	public Bool() {
		
		value = null;
	}
	
	/**
	 * Creates a new Bool instance with the given value.
	 *
	 * @param value value
	 */
	public Bool(@Nullable Boolean value) {
		
		this.value = value;
	}
	
	@Nullable
	@Override
	public Boolean getObject() {
		
		return value;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(value);
	}
	
	@Override
	public boolean equals(Object o) {
		
		return o instanceof Bool && Objects.equals(value, ((Bool) o).value);
	}
	
	/**
	 * @return {@code false} if the held value is null, or the held value.
	 */
	public boolean bool() {
		
		return value != null && value;
	}
	
	/**
	 * Tests if the held value is true.
	 *
	 * @return {@code false} if the held value is null,
	 * 		or {@code true} if the held value is true.
	 */
	public boolean isTrue() {
		
		return bool();
	}
	
	/**
	 * Tests if the held value is false.
	 *
	 * @return {@code true} if the held value is null,
	 * 		or if the held value is false.
	 */
	public boolean isFalse() {
		
		return !bool();
	}
	
	/**
	 * Creates a new Bool instance.
	 *
	 * @param value value
	 * @return Bool instance
	 */
	public static Bool of(boolean value) {
		
		return value ? TRUE : FALSE;
	}
	
	/**
	 * Creates a new Bool instance.
	 *
	 * @param value value
	 * @return Bool instance
	 */
	public static Bool of(@Nullable Boolean value) {
		
		return value != null ? of(value.booleanValue()) : NONE;
	}
	
	/**
	 * Creates a new Bool instance.
	 *
	 * @param value value. Non-zero value means true.
	 * @return Bool instance
	 */
	public static Bool of(int value) {
		
		return of(value != 0);
	}
	
	/**
	 * Creates a new Bool instance.
	 *
	 * @param value value. Non-zero value means true.
	 * @return Bool instance
	 */
	public static Bool of(long value) {
		
		return of(value != 0L);
	}
	
	/**
	 * Creates a new Bool instance.
	 *
	 * @param value value. Non-null value means true.
	 * @param <T>   type of object
	 * @return Bool instance
	 */
	public static <T> Bool of(T value) {
		
		return of(value != null);
	}
}
