package com.tr.hsyn.gate.dev;


import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.gate.AutoGate;


public class SimpleAutoGate extends SimpleGate implements AutoGate {
	
	/**
	 * Kapının açılması için geçmesi gereken süre
	 */
	protected final long interval;
	
	public SimpleAutoGate() {
		
		interval = 0L;
	}
	
	public SimpleAutoGate(long passInterval) {
		
		interval = passInterval;
	}
	
	@Override
	public final long getInterval() {
		
		return interval;
	}
	
	@Override
	public boolean enter() {
		
		if (super.enter()) {
			
			Runny.run(this::exit, interval);
			return true;
		}
		
		return false;
	}
	
	@Override
	public final boolean enter(long outAfterMillis) {
		
		if (super.enter()) {
			
			Runny.run(this::exit, outAfterMillis);
			
			return true;
		}
		
		return false;
	}
}
