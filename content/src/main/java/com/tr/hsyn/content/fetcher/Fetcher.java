package com.tr.hsyn.content.fetcher;


import androidx.annotation.NonNull;

import java.util.List;


public interface Fetcher <T>{
	
	@NonNull
	List<T> fetch();
}
