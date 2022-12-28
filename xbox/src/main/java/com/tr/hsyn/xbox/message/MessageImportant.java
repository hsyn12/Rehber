package com.tr.hsyn.xbox.message;


import org.jetbrains.annotations.NotNull;


public class MessageImportant extends Message {
	
	public MessageImportant(@NotNull CharSequence title, @NotNull CharSequence message) {
		
		super(title, message);
	}
	
	public MessageImportant(@NotNull CharSequence message) {
		
		super(message);
	}
}
