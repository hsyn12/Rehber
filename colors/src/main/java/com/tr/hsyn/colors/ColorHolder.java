package com.tr.hsyn.colors;


/**
 * Uygulamanın bir çok yerinde kullanılan bazı renk bilgilerini sağlar.
 */
public interface ColorHolder {
	
	/**
	 * @return Ana renk
	 */
	int getPrimaryColor();
	
	/**
	 * @return Bir önceki ana renk
	 */
	int getLastColor();
	
	/**
	 * @return Ripple resource
	 */
	int getRipple();
	
	static ColorHolder newHolder(int primaryColor, int lastColor, int ripple) {
		
		return new Colorsky(primaryColor, lastColor, ripple);
	}
	
}
