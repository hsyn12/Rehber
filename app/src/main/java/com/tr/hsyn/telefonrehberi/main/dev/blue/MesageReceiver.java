package com.tr.hsyn.telefonrehberi.main.dev.blue;


import com.tr.hsyn.key.Key;
import com.tr.hsyn.xbox.Data;
import com.tr.hsyn.xbox.message.Message;
import com.tr.hsyn.xbox.receiver.Receiver;
import com.tr.hsyn.xlog.xlog;


public class MesageReceiver implements Receiver {
	
	@Override
	public Key getKey() {
		
		return Key.MESSAGE;
	}
	
	@Override
	public <O> O onReceive(Data data) {
		
		var labels = data.getLabels();
		
		if (data.getObject() != null) {
			
			Message message = (Message) data.getObject();
			
			xlog.d("%s : %s", message.getFrom(), message.getMessage());
		}
		
		
		return null;
	}
	
	@Override
	public int getId() {
		
		return 1243;
	}
}
