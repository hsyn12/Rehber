package com.tr.hsyn.life;


import com.tr.hsyn.registery.cast.Database;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public interface LifeRecorder extends Database<Life> {
	
	boolean endLife(@NotNull Life live);
	
	List<Life> getLifes();
	
	List<Life> getLifes(@NotNull String name);
	
	Life findLife(long start);
	
	Life findLife(@NotNull Life aLife);
}
