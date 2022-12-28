package com.tr.hsyn.colors;


import androidx.annotation.NonNull;

import com.tr.hsyn.life.Life;
import com.tr.hsyn.life.LifeTime;


public interface ColorRegister {
	
	String getName();
	
	int getColor();
	
	Life getLifeTime();
	
	@NonNull
	static ColorRegister newRegister(@NonNull String name, int color, @NonNull Life lifeTime) {
		
		return new ColorData(name, color, lifeTime);
	}
	
}
