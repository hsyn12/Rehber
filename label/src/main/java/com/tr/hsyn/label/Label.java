package com.tr.hsyn.label;


import com.tr.hsyn.atom.Atom;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


/**
 * Etiket.<br>
 * Nesnelerin işareti.
 */
public class Label extends Atom {
	
	/**
	 * Geçersiz id değeri. Bu değeri taşıyan tek etiket {@link #INVALID_LABEL} nesnesidir ve
	 * bu id başka hiçbir etiket için kullanılmamalıdır.
	 */
	public static final int INVALID_LABEL_ID = -1;
	
	/**
	 * Geçersiz etiket
	 */
	public static final Label INVALID_LABEL = newLabel(-1, "");
	
	public Label(long id, @NotNull String name) {
		
		super(id, name);
	}
	
	@NotNull
	public static Label newLabel(long id, @NotNull String name) {
		
		return new Label(id, name);
	}
	
	@NotNull
	public static Set<Label> of(Label... labels) {
		
		return new HashSet<>(Arrays.asList(labels));
	}
	
	/**
	 * String bir nesneyi etikete çevirir.
	 * String olarak etiket {@code 'id:name'} şeklinde olmalı.
	 *
	 * @param label Etiket bildiren string ({@code 'id:name'})
	 * @return {@code Label} nesnesi
	 */
	@NotNull
	public static Label fromString(@NotNull String label) {
		
		try {
			
			var parts = label.split(":");
			
			return newLabel(Integer.parseInt(parts[0]), parts[1]);
		}
		catch (Exception ignore) {}
		
		return Label.newLabel(-1, "-");
	}
	
	@Override
	public @NotNull String toString() {
		
		return String.format(Locale.getDefault(), "%d:%s", getId(), getName());
	}
	
	/**
	 * @return Etiket geçerli bir etiket ise {@code true}, değilse {@code false}
	 */
	public boolean isValid() {
		
		return !this.equals(INVALID_LABEL);
	}
	
	/**
	 * @return Etiket geçersiz ise {@code true}, geçerli ise {@code false}
	 */
	public boolean isInValid() {
		
		return this.equals(INVALID_LABEL);
	}
}