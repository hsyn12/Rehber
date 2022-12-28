package com.tr.hsyn.xbox;


import com.tr.hsyn.key.Key;
import com.tr.hsyn.label.Label;
import com.tr.hsyn.label.Mabel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;


/**
 * Veri
 */
public class Data implements Mabel {
	
	private final long       time = System.currentTimeMillis();
	private final Key        key;
	private final Object     object;
	private       Set<Label> labels;
	
	public Data(@NotNull Key key) {
		
		this.key    = key;
		this.object = null;
	}
	
	public Data(@NotNull Key key, @Nullable Object object) {
		
		this.key    = key;
		this.object = object;
	}
	
	public long getTime() {
		
		return time;
	}
	
	@Nullable
	public Object getObject() {return object;}
	
	public Key getKey() {return key;}
	
	@Override
	public @Nullable Set<Label> getLabels() {return labels;}
	
	@Override
	public void setLabels(@Nullable Set<Label> labels) {this.labels = labels;}
	
	@NotNull
	public static Data create(@NotNull Key key) {
		
		return new Data(key);
	}
	
	@NotNull
	public static Data create(@NotNull Key key, @Nullable Object object) {
		
		return new Data(key, object);
	}
	
}
