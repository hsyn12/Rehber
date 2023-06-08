package com.tr.hsyn.xbox;


import com.tr.hsyn.xlog.xlog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class Lobby {
	
	public final  Map<com.tr.hsyn.key.Key, List<Consumer<Object>>> WAITING_ROOM = new HashMap<>();
	@SuppressWarnings("FieldCanBeLocal")
	private final int                                              DEBUG_DEGREE = 0;
	
	@SuppressWarnings("ConstantConditions")
	public <T> boolean lookFor(com.tr.hsyn.key.Key key, T object) {
		
		if (WAITING_ROOM.get(key) != null) {
			
			WAITING_ROOM.remove(key).forEach(follower -> follower.accept(object));
			
			if (DEBUG_DEGREE > 0)
				xlog.i("Meeting is completed for [%s]", key);
			
			return true;
		}
		else {
			
			if (DEBUG_DEGREE > 1)
				xlog.i("No meeting for [%s]", key.getName());
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public <T> void waitFor(com.tr.hsyn.key.Key key, Consumer<T> consumer) {
		
		List<Consumer<Object>> followers = this.WAITING_ROOM.get(key);
		
		if (followers != null) {
			
			if (!followers.contains(consumer)) {
				
				followers.add((Consumer<Object>) consumer);
			}
		}
		else {
			
			followers = new ArrayList<>(1);
			followers.add((Consumer<Object>) consumer);
			this.WAITING_ROOM.put(key, followers);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T exit(com.tr.hsyn.key.Key key) {
		
		return (T) WAITING_ROOM.remove(key);
	}
	
	public void close() {
		
		WAITING_ROOM.clear();
	}
	
}
