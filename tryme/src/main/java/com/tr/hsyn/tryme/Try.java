package com.tr.hsyn.tryme;


import org.jetbrains.annotations.NotNull;


public class Try {
	
	/**
	 * Verilen işi çalıştırır ve hataları görmezden gelir.
	 *
	 * @param runnable Hata üretebilecek bir iş
	 */
	public static void ignore(@NotNull final Runnable runnable) {
		
		try {runnable.run();}
		catch (Exception ignore) {}
	}
}
