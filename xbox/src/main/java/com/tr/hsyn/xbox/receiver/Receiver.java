package com.tr.hsyn.xbox.receiver;


import com.tr.hsyn.identity.Identity;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.xbox.Data;


public interface Receiver extends Identity {
	
	Key getKey();
	
	<O> O onReceive(Data data);
}
