package com.tr.hsyn.gate.dev;


import com.tr.hsyn.gate.DelayedRunner;
import com.tr.hsyn.gate.DigiGate;
import com.tr.hsyn.gate.GateObserver;
import com.tr.hsyn.gate.ObserableGate;
import com.tr.hsyn.watch.observable.Observable;
import com.tr.hsyn.watch.observable.Observe;

import org.jetbrains.annotations.NotNull;


public class SimpleDigiGate extends SimpleAutoGate implements DigiGate, ObserableGate {
	
	protected final Observable<Boolean> gateState = new Observe<>(true);
	protected       boolean             loopDelay;
	protected       DelayedRunner       delayedRunner;
	private         GateObserver        gateObserver;
	private         Runnable            onExit;
	
	public SimpleDigiGate() {
		
		this(0L, null);
	}
	
	public SimpleDigiGate(Runnable onExit) {
		
		this(0L, onExit);
	}
	
	public SimpleDigiGate(long passInterval) {
		
		this(passInterval, null);
	}
	
	public SimpleDigiGate(long passInterval, Runnable onExit) {
		
		super(passInterval);
		gateState.setObserver(this::onUpdate);
		this.onExit = onExit;
	}
	
	@Override
	protected void open() {
		
		super.open();
		
		if (delayedRunner != null)
			delayedRunner.setDone(false);
		
		gateState.set(true);
	}
	
	@Override
	protected void close() {
		
		super.close();
		gateState.set(false);
	}
	
	@Override
	public void exit() {
		
		super.exit();
		gateState.set(true);//Çıktığında kapı açılıyor
	}
	
	@Override
	public void setGateObserver(GateObserver gateObserver) {
		
		this.gateObserver = gateObserver;
	}
	
	@Override
	public boolean enter() {
		
		if (super.enter()) {
			
			//Kapı izleniyor
			gateState.set(false);//Girdiğinde kapı kapanıyor
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean enter(@NotNull Runnable onExit) {
		
		return enter(getInterval(), onExit);
	}
	
	@Override
	public void setOnExit(Runnable onExit) {
		
		this.onExit = onExit;
	}
	
	@Override
	public DigiGate loop() {
		
		delayedRunner = new DelayedRunner();
		loopDelay     = true;
		return this;
	}
	
	protected void onUpdate(Boolean isOpen) {
		
		if (gateObserver != null) gateObserver.onGateStateChange(isOpen);
		
		if (isOpen && onExit != null) onExit.run();
	}
	
	private boolean enter(long outAfter, @NotNull Runnable onExit) {
		
		if (loopDelay) {
			
			if (isOpen()) {
				
				close();
				this.onExit = onExit;
				delayedRunner.setAction(this::open).run(outAfter);
				return true;
			}
			else {
				
				delayedRunner.cancel();
				delayedRunner.run(outAfter);
			}
		}
		else {
			
			if (enter()) {
				
				this.onExit = onExit;
				return true;
			}
		}
		
		return false;
	}
	
}
