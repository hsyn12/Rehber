package com.tr.hsyn.xbox.message;


import org.jetbrains.annotations.NotNull;


public class MessageWarn extends Message {
	
	public MessageWarn(@NotNull CharSequence title, @NotNull CharSequence message) {
		
		super(title, message);
	}
	
	public MessageWarn(@NotNull CharSequence message) {
		
		super(message);
	}
}
