package com.tr.hsyn.db.actor;


import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.function.Function;


public class Operator<R> implements OCreator<R> {
	
	private final Cursor                        cursor;
	private final Function<Cursor, ? extends R> function;
	
	public Operator(@NonNull Cursor cursor, @NonNull Function<Cursor, ? extends R> function) {
		
		this.cursor   = cursor;
		this.function = function;
	}
	
	@NonNull
	@Override
	public Function<Cursor, ? extends R> getObjectFunction() {
		
		return function;
	}
	
	@NonNull
	@Override
	public Cursor getCursor() {
		
		return cursor;
	}
}
