package com.tr.hsyn.use;


import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


public class Use {
	
	private final boolean used;
	
	protected Use(boolean used) {
		
		this.used = used;
	}
	
	/**
	 * Verilen nesne {@code null} değilse kullanılır.
	 *
	 * @param object     Test edilecek nesne
	 * @param objectUser Nesne {@code null} değilse kullanacak olan nesne
	 * @param <T>        Test edilecek nesnenin sınıf türü
	 * @return Test edilen nesne {@code null} değilse, bu nesnenin kullanıldığına işaret eden {@code true} bir değer ile yeni bir Use nesnesi döner.
	 * 		Eğer test edilen nesne {@code null} ise bu nesnenin kullanılmadığına işaret eden {@code false} bir değer ile yeni bir Use nesnesi döner.
	 */
	@NotNull
	public static <T> Use ifNotNull(T object, Consumer<T> objectUser) {
		
		if (object != null) {
			
			if (objectUser != null) {
				
				objectUser.accept(object);
			}
			
			return new Use(true);
		}
		
		return new Use(false);
	}
	
	/**
	 * Test edilen nesne kullanıldı ise verilen işi çalıştırır.
	 *
	 * @param runnable İş
	 * @return This
	 */
	public Use isUsed(Runnable runnable) {
		
		if (used && runnable != null) runnable.run();
		
		return this;
	}
	
	/**
	 * Test edilen nesne kullanılmadı ise verilen işi çalıştırır.
	 *
	 * @param runnable İş
	 * @return This
	 */
	public Use isNotUsed(Runnable runnable) {
		
		if (!used && runnable != null) runnable.run();
		
		return this;
	}
}
