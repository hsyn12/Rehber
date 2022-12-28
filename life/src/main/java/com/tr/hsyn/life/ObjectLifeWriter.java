package com.tr.hsyn.life;


import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;


public class ObjectLifeWriter implements LifeWriter {
	
	@NotNull
	private final LifeRecorder          lifeDatabase;
	@NotNull
	private final AtomicReference<Life> life = new AtomicReference<>();
	
	public ObjectLifeWriter(@NotNull LifeRecorder lifeDatabase, @NotNull String liveName) {
		
		this.lifeDatabase = lifeDatabase;
		this.life.set(Life.newLife(liveName));
	}
	
	@Override
	public @NotNull LifeRecorder getLifeRecorder() {
		
		return lifeDatabase;
	}
	
	@Override
	public @NotNull Life getLife() {
		
		return life.get();
	}
}
