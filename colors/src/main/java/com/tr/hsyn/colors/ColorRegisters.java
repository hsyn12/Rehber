package com.tr.hsyn.colors;


import android.content.Context;

import androidx.annotation.NonNull;

import com.tr.hsyn.registery.cast.Database;


public interface ColorRegisters extends Database<ColorRegister> {
	
	@NonNull
	static ColorRegisters getRegisters(@NonNull final Context context) {
		
		com.tr.hsyn.holder.Holder<ColorRegisters> holder = RegistersHolder.colorRegistersHolder.get();
		
		if (holder.getValue() == null)
			holder.setValue(new Colorabi(context));
		
		//noinspection ConstantConditions
		return holder.getValue();
	}
}
