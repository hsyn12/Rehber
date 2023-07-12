package com.tr.hsyn.label;


import com.tr.hsyn.atom.AtomImpl;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


/**
 * Label of things.<br>
 * Provides to define the labels and check their validations.<br>
 * A label is a thing.
 * It provides to separate or to group the things.
 */
public class Label extends AtomImpl {
	
	/**
	 * The invalid label.
	 */
	public static final Label INVALID_LABEL = of(-1, "");
	
	/**
	 * Constructor for label.
	 *
	 * @param id   id
	 * @param name name
	 */
	public Label(long id, @NotNull String name) {
		
		super(id, name);
	}
	
	@Override
	public @NotNull String toString() {
		
		return String.format(Locale.getDefault(), "%d:%s", getId(), getName());
	}
	
	/**
	 * @return {@code true} if label is valid, {@code false} otherwise
	 */
	public boolean isValid() {
		
		return !this.equals(INVALID_LABEL);
	}
	
	/**
	 * @return {@code true} if label is invalid, {@code false} otherwise
	 */
	public boolean isInValid() {
		
		return this.equals(INVALID_LABEL);
	}
	
	/**
	 * Creates a new label.
	 *
	 * @param id   id
	 * @param name name
	 * @return new {@code Label}
	 */
	@NotNull
	public static Label of(long id, @NotNull String name) {
		
		return new Label(id, name);
	}
	
	/**
	 * Converts a String object to a label.
	 * As a string, the tag must be in the form {@code 'id:name'}.
	 *
	 * @param label the string in the form ({@code 'id:name'})
	 * @return {@code Label} object
	 */
	@NotNull
	public static Label of(@NotNull String label) {
		
		try {
			
			String[] parts = label.split(":");
			
			return of(Integer.parseInt(parts[0]), parts[1]);
		}
		catch (Exception ignore) {}
		
		return INVALID_LABEL;
	}
}