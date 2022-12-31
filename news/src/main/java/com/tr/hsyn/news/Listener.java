package com.tr.hsyn.news;


import org.jetbrains.annotations.NotNull;


public interface Listener {
	
	void onNews(Object... args);
	
	@NotNull Channel getChannel();
}
