package com.tr.hsyn.news.collector;


import com.tr.hsyn.news.Sheet;
import com.tr.hsyn.news.dispatcher.DispatchEditor;

import org.jetbrains.annotations.NotNull;


/**
 * Tüm haberleri toplayan
 */
public interface NewsCollector extends DispatchEditor {
	
	/**
	 * Yeni habel topla.
	 *
	 * @param sheet Haber
	 */
	void collect(@NotNull Sheet sheet);
	
}
