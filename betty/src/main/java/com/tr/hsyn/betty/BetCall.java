package com.tr.hsyn.betty;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;


public class BetCall<R> extends Bet<R> {
	
	protected BetCall(@Nullable final Exception e, @Nullable R v, @Nullable Boolean out) {
		
		super(e, v, out);
	}
	
	@Override 
	public Bet<R> onSuccess(@NotNull final Consumer<R> consumer) {
		
		//- Eğer kontrol edilecek birşey varsa ve bu false ise çağrılmayacak
		//- Eğer işlem hata üretmiş ise çağrılmayacak
		
		if (out != null) {
			
			if (out && e == null) consumer.accept(v);
		}
		else {
			
			if (e == null)
				consumer.accept(v);
		}
		
		return this;
	}
}
