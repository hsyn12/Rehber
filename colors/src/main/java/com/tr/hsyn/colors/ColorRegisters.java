package com.tr.hsyn.colors;


import android.content.Context;

import androidx.annotation.NonNull;

import com.tr.hsyn.registery.cast.Database;


public interface ColorRegisters extends Database<ColorRegister> {
	
	@NonNull
	static ColorRegisters getRegisters(@NonNull final Context context) {
		
		var holder = RegistersHolder.colorRegistersHolder.get();
		
		if (holder.get() == null)
			holder.set(new Colorabi(context));
		
		//noinspection ConstantConditions
		return holder.get();
	}
}
