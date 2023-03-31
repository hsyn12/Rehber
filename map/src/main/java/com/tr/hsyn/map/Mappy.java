package com.tr.hsyn.map;


import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class Mappy {
	
	@NotNull
	public static <K, V> Map<K, V> newMap() {
		
		return new HashMap<>();
	}
}