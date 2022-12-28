package com.tr.hsyn.xbox.message;


import org.jetbrains.annotations.NotNull;


public class MessageInfo extends Message {
	
	public MessageInfo(@NotNull CharSequence title, @NotNull CharSequence message) {
		
		super(title, message);
	}
	
	public MessageInfo(@NotNull CharSequence message) {
		
		super(message);
	}
}
