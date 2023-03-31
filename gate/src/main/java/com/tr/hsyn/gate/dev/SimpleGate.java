package com.tr.hsyn.gate.dev;


import com.tr.hsyn.gate.Gate;

import java.util.concurrent.atomic.AtomicBoolean;


public class SimpleGate implements Gate {
	
	private final AtomicBoolean state = new AtomicBoolean(true);
	
	protected void open() {
		
		state.set(true);
	}
	
	protected void close() {
		
		state.set(false);
	}
	
	@Override
	public boolean isOpen() {
		
		return state.get();
	}
	
	@Override
	public boolean enter() {
		
		if (isOpen()) {
			
			close();
			return true;
		}
		
		return false;
	}
	
	@Override
	public void exit() {
		
		open();
	}
}
