package com.tr.hsyn.telefonrehberi.main.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Mapple;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * Holds the calls in a map for faster access.
 */
public class CallMap extends Groups<String, Call> {
	
	/**
	 * Creates a new instance.
	 *
	 * @param calls the calls
	 */
	public CallMap(@NotNull List<Call> calls, @NotNull Function<Call, String> keyFunction) {
		
		super(Mapple.groupsFrom(calls, keyFunction));
	}
	
	public CallMap(@NotNull Map<String, List<Call>> callMap) {
		
		super(callMap);
	}
	
}
