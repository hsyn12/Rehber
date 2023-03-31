package com.tr.hsyn.colors;


import androidx.annotation.NonNull;

import com.tr.hsyn.identity.Identity;
import com.tr.hsyn.life.Life;


public interface ColorRegister extends Identity {
	
	String getName();
	
	int getColor();
	
	Life getLifeTime();
	
	@NonNull
	static ColorRegister newRegister(@NonNull String name, int color, @NonNull Life lifeTime) {
		
		return new ColorData(name, color, lifeTime);
	}
	
}
