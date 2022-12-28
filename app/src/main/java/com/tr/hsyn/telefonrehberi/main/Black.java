package com.tr.hsyn.telefonrehberi.main;


import com.tr.hsyn.news.News;
import com.tr.hsyn.telefonrehberi.main.activity.city.BlackTower;


/**
 * Uygulamanın giriş noktası.
 */
public class Black extends BlackTower {
	
	@Override
	protected void onCreate() {
		
		super.onCreate();
		
		News.shot("hello from mee", News.IMPORTANT);
	}
}
