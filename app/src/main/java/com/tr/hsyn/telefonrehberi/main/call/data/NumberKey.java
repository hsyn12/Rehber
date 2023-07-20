package com.tr.hsyn.telefonrehberi.main.call.data;


import androidx.annotation.Nullable;

import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Holds the numbers as a key.
 */
public class NumberKey {
	
	/**
	 * The keys.
	 */
	private final List<String> keys;
	
	/**
	 * Creates an instance.
	 *
	 * @param numbers phone number list
	 */
	public NumberKey(@NotNull List<String> numbers) {
		
		this.keys = new ArrayList<>(numbers.size());
		
		Lister.loopWith(numbers, this::addKey);
	}
	
	@Override
	public int hashCode() {
		
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(@Nullable Object obj) {
		
		return obj instanceof NumberKey && toString().equals(obj.toString());
	}
	
	@NotNull
	@Override
	public String toString() {
		
		return Stringx.join(this.keys, "");
	}
	
	/**
	 * @return the keys
	 */
	private List<String> getKeys() {
		
		return keys;
	}
	
	/**
	 * Checks if the given number is contained in the list.
	 *
	 * @param number the number
	 * @return {@code true} if the number is contained
	 */
	public boolean contains(String number) {
		
		return keys.contains(getKey(number));
	}
	
	/**
	 * Returns the key.
	 *
	 * @return the key
	 */
	public String getKey() {
		
		return toString();
	}
	
	/**
	 * Adds a key to the list.
	 *
	 * @param number the number
	 */
	private void addKey(@NotNull String number) {
		
		this.keys.add(getKey(number));
	}
	
	private @NotNull String getKey(@NotNull String number) {
		
		return PhoneNumbers.formatNumber(number, PhoneNumbers.MINIMUM_NUMBER_LENGTH);
	}
}
