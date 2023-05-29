package com.tr.hsyn.page;


import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Liste sahibi.
 *
 * @param <T> Liste eleman türü
 */
public interface HaveList<T> {
	
	T getItem(int index);
	
	default int indexOfItem(T item) {
		
		return getList().indexOf(item);
	}
	
	/**
	 * @return Liste
	 */
	List<T> getList();
	
	/**
	 * Listeyi kaydet.
	 *
	 * @param list Liste
	 */
	void setList(@NotNull List<T> list);
	
	default int getListSize() {
		
		return getList() != null ? getList().size() : 0;
	}
	
}