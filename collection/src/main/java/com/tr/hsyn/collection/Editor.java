package com.tr.hsyn.collection;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public interface Editor {
	
	@Nullable
	static <T> T remove(List<T> list, int index) {
		
		if (isIndex(list, index)) return list.remove(index);
		
		return null;
	}
	
	static <T> boolean isIndex(@NotNull List<T> list, int index) {
		
		return index < list.size() && index >= 0;
	}
}
