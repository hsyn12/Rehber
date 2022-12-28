package com.tr.hsyn.xbox.message;


import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/**
 * Message Object
 */
public class Message {
	
	private final long         time;
	private final CharSequence from;
	private final CharSequence message;
	
	public Message(@NotNull CharSequence message) {
		
		this.time    = System.currentTimeMillis();
		this.from    = "-";
		this.message = message;
	}
	
	public Message(@NotNull CharSequence from, @NotNull CharSequence message) {
		
		this.time    = System.currentTimeMillis();
		this.from    = from;
		this.message = message;
	}
	
	public Message(long time, @NotNull CharSequence from, @NotNull CharSequence message) {
		
		this.time    = time;
		this.from    = from;
		this.message = message;
	}
	
	public long getTime() {
		
		return time;
	}
	
	@NotNull
	public CharSequence getFrom() {
		
		return from;
	}
	
	@NotNull
	public CharSequence getMessage() {
		
		return message;
	}
	
	public boolean isInfo() {
		
		return this instanceof MessageInfo;
	}
	
	public boolean isWarn() {
		
		return this instanceof MessageWarn;
	}
	
	public boolean isImportant() {
		
		return this instanceof MessageImportant;
	}
	
	@Override
	public boolean equals(Object o) {
		
		return o instanceof Message && time == ((Message) o).time;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(time);
	}
	
	@NotNull
	@Override
	public String toString() {
		
		return "Message{" +
		       "time=" + time +
		       ", from=" + from +
		       ", message=" + message +
		       '}';
	}
	
	@NotNull
	public static Message info(@NotNull CharSequence message) {
		
		return new MessageInfo(message);
	}
	
	@NotNull
	public static Message info(@NotNull CharSequence from, @NotNull CharSequence message) {
		
		return new MessageInfo(from, message);
	}
	
	@NotNull
	public static Message warn(@NotNull CharSequence message) {
		
		return new MessageWarn(message);
	}
	
	@NotNull
	public static Message warn(@NotNull CharSequence from, @NotNull CharSequence message) {
		
		return new MessageWarn(from, message);
	}
	
	@NotNull
	public static Message important(@NotNull CharSequence message) {
		
		return new MessageImportant(message);
	}
	
	@NotNull
	public static Message important(@NotNull CharSequence from, @NotNull CharSequence message) {
		
		return new MessageImportant(from, message);
	}
	
}
