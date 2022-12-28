package com.tr.hsyn.news;


import com.tr.hsyn.codefinder.CodeLocation;
import com.tr.hsyn.label.Label;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;


/**
 * Kısa haber mesajı
 */
public class NewsShot {
	
	/**
	 * Stack index for {@link CodeLocation}
	 */
	private static final int stackIndex = 5;
	
	/**
	 * Kısa haber gönderir.
	 *
	 * @param message Mesaj
	 * @param labels  Etiketler
	 */
	public static void shot(@NotNull CharSequence message, @NotNull Label... labels) {
		
		var sheet = new Sheet(message, stackIndex);
		
		if (labels.length > 0) {
			
			sheet.setLabels(new HashSet<>(Arrays.asList(labels)));
		}
		
		News.sheet(sheet);
	}
	
	/**
	 * Kısa haber gönderir.
	 *
	 * @param title   Başlık
	 * @param message Mesaj
	 * @param labels  Etiketler
	 */
	public static void shot(@NotNull CharSequence title, @NotNull CharSequence message, @NotNull Label... labels) {
		
		var sheet = new Sheet(title, message, stackIndex);
		
		if (labels.length > 0) {
			
			sheet.setLabels(new HashSet<>(Arrays.asList(labels)));
		}
		
		News.sheet(sheet);
	}
}
