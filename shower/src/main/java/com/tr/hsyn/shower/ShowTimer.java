package com.tr.hsyn.shower;


/**
 * Bir nesnenin gösterim durumunu kontrol eder.
 */
public interface ShowTimer {
	
	/**
	 * Gösterim durumunu bildirir.
	 *
	 * @param showTime Gösterimde ise {@code true} değilse {@code false}
	 */
	void showTime(boolean showTime);
	
	boolean isShowTime();
}
