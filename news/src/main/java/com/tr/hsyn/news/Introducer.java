package com.tr.hsyn.news;


public interface Introducer {
	
	default void introduce(CharSequence subject, Object object) {
		
		//News.sheet(new Sheet(subject, object));
	}
	
}
