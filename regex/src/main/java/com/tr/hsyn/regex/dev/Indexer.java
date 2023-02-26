package com.tr.hsyn.regex.dev;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public interface Indexer {
	
	/**
	 * Bir string nesnenin karakterlerini index numaralarıyla eşleştiren bir liste döndürür.
	 *
	 * @param text String nesne
	 * @return Index listesi
	 */
	static @NotNull List<Indexy> withIndex(@NotNull String text) {
		
		List<Indexy> indexies = new ArrayList<>();
		
		int index = 0;
		for (var c : text.toCharArray())
			indexies.add(new IndexedChar(c, index++));
		
		return indexies;
	}
}
