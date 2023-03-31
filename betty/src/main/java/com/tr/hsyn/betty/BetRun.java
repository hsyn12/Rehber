package com.tr.hsyn.betty;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class BetRun<R> extends Bet<R> {
	
	protected BetRun(@Nullable final Exception e, @Nullable R v, @Nullable Boolean out) {
		
		super(e, v, out);
	}
	
	@Override
	public Bet<R> onSuccess(@NotNull Runnable action) {
		
		if (out != null) {
			
			if (out && e == null) action.run();
		}
		else {
			
			if (e == null)
				action.run();
		}
		
		return this;
	}
}
