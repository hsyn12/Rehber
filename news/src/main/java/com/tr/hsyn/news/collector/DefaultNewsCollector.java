package com.tr.hsyn.news.collector;


import com.tr.hsyn.news.Sheet;
import com.tr.hsyn.news.dispatcher.DefaultNewsDispatcher;
import com.tr.hsyn.news.dispatcher.NewsDispatcher;

import org.jetbrains.annotations.NotNull;


public class DefaultNewsCollector implements NewsCollector {
	
	private NewsDispatcher dispatcher = new DefaultNewsDispatcher();
	
	@Override
	public void collect(@NotNull Sheet sheet) {
		
		dispatcher.dispatch(sheet);
	}
	
	@Override
	public @NotNull NewsDispatcher getDispatcher() {
		
		return dispatcher;
	}
	
	@Override
	public void setDispatcher(@NotNull NewsDispatcher dispatcher) {
		
		this.dispatcher = dispatcher;
	}
}
