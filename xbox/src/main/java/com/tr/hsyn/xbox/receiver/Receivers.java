package com.tr.hsyn.xbox.receiver;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class Receivers {
	
	private final List<Receiver> receivers = new ArrayList<>();
	
	public void add(@NotNull Receiver receiver) {
		
		receivers.add(receiver);
	}
	
	public boolean remove(@NotNull Receiver receiver) {
		
		return receivers.remove(receiver);
	}
	
	
}
