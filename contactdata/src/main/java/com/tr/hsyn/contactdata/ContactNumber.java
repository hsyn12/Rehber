package com.tr.hsyn.contactdata;


import org.jetbrains.annotations.Nullable;

import java.util.List;


public interface ContactNumber {
	
	@Nullable
	List<String> getNumbers();
	
	void setNumbers(@Nullable List<String> numbers);
}
