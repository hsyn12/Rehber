package com.tr.hsyn.colors;


import androidx.annotation.NonNull;

import com.tr.hsyn.life.Life;


public class ColorData implements ColorRegister {
	
	private final String name;
	private final int    color;
	private final Life   lifeTime;
	
	public ColorData(@NonNull String name, int color, @NonNull Life lifeTime) {
		
		this.name     = name;
		this.color    = color;
		this.lifeTime = lifeTime;
	}
	
	@Override
	public String getName() {
		
		return name;
	}
	
	@Override
	public int getColor() {
		
		return color;
	}
	
	@Override
	public Life getLifeTime() {
		
		return lifeTime;
	}
	
	@Override
	public long getId() {
		
		return lifeTime.getId();
	}
}
