package com.tr.hsyn.telefonrehberi.main.cast;


import java.util.List;


public interface ListAdapter<T> {
	
	void notifyItemChanged(int index);
	
	void notifyItemRemoved(int index);
	
	void notifyItemRangeRemoved(int indexStart, int indexEnd);
	
	int getSize();
	
	List<T> getItems();
	
	void setItems(List<T> items);
	
	void clearItems();
}
