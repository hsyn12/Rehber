package com.tr.hsyn.message.internal;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.message.GlobalMessage;
import com.tr.hsyn.xlog.xlog;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;


/**
 * <h1>MessageBox</h1>
 *
 * <p>
 * Mesaj kutusu.
 *
 * @author hsyn 2019-12-09 13:53:56
 */
public class MessageBox {
	
	
	private final List<GlobalMessage>     messageList = new LinkedList<>();
	private       Consumer<GlobalMessage> action;
	
	public Consumer<GlobalMessage> getAction() {
		
		return action;
	}

	public void setAction(Consumer<GlobalMessage> action) {
		
		this.action = action;
	}
	
	public void add(@NonNull String message) {
		
		add(new GlobalMessage(message));
	}
	
	public void add(@NonNull GlobalMessage message) {
		
		messageList.add(message);
		
		if (action != null) action.accept(message);
	}
	
	public void add(String subject, String message) {
		
		add(GlobalMessage.create(subject, message));
	}
	
	public void add(String message, @GlobalMessage.Type int type) {
		
		add(GlobalMessage.create(message, type));
	}
	
	public void add(String subject, String message, @GlobalMessage.Type int type) {
		
		add(GlobalMessage.create(subject, message, type));
	}
	
	@Nullable
	public GlobalMessage get() {
		
		if (size() != 0) {
			
			GlobalMessage message = messageList.remove(0);
			
			xlog.d("Mesaj kutusunda kalan mesaj sayısı : %d", messageList.size());
			
			return message;
		}
		
		return null;
	}
	
	public int size() {
		
		return messageList.size();
	}
	
	
}
