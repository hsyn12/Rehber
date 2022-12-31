package com.tr.hsyn.news;


import com.tr.hsyn.news.collector.NewsCollector;

import org.jetbrains.annotations.NotNull;


public interface NewsEditor {
	
	@NotNull NewsCollector getNewsCollector();
	
	void setNewsCollector(@NotNull NewsCollector collector);
}
