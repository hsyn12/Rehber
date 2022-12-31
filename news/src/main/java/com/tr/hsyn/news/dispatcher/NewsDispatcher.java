package com.tr.hsyn.news.dispatcher;


import com.tr.hsyn.news.Sheet;

import org.jetbrains.annotations.NotNull;


/**
 * Haberlerin dağıtıcısı
 */
public interface NewsDispatcher {
	
	/**
	 * Haberi dağıt.
	 *
	 * @param sheet Haber
	 */
	void dispatch(@NotNull Sheet sheet);
}
