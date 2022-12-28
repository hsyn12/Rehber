package com.tr.hsyn.content.server;


import androidx.annotation.NonNull;

import java.util.List;


public interface Server<T>{
	
	@NonNull
	List<T> getContents();
}
