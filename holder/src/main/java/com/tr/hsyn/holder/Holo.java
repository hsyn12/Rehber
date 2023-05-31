package com.tr.hsyn.holder;


/**
 * This is a generic class that implements the {@link Holder} interface.
 * It can hold a value of any type T.
 *
 * @param <T> the type of the value to hold
 */
public class Holo<T> implements Holder<T> {
	
	/**
	 * The value held by this object
	 */
	private T value;
	
	/**
	 * Constructs an empty Holo object.
	 */
	public Holo() {}
	
	/**
	 * Constructs new {@link Holo} object with the specified value.
	 *
	 * @param value the value to hold
	 */
	public Holo(T value) {
		
		this.value = value;
	}
	
	/**
	 * Returns the value held by this Holo object.
	 *
	 * @return the value held by this Holo object
	 */
	@Override
	public T getValue() {
		
		return value;
	}
	
	/**
	 * Sets the value held by this Holo object to the specified value.
	 *
	 * @param t the value to set
	 */
	@Override
	public void setValue(T t) {
		
		value = t;
	}
}
