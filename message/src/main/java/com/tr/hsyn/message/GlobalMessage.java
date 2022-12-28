package com.tr.hsyn.message;


import android.view.Gravity;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class GlobalMessage {
	
	/**
	 * Bilgi mesajı.
	 */
	public static final int          INFO     = 0;
	/**
	 * Uyarı mesajı.
	 */
	public static final int          WARN     = 1;
	/**
	 * Hata mesajı.
	 */
	public static final int          ERROR    = 2;
	private             CharSequence subject;
	private             CharSequence message;
	private             int          type     = INFO;
	private             CharSequence actionText;
	private             Runnable     action;
	private             boolean      snake;
	private             long         duration = 4_000L;
	private             long         delay    = 100L;
	private             int          position = Gravity.TOP;
	private int relationDegree = 1;
	
	public GlobalMessage(CharSequence message) {
		
		this.message = message;
	}
	public GlobalMessage(CharSequence subject, CharSequence message) {
		
		this.message = message;
		this.subject = subject;
	}
	public GlobalMessage(CharSequence subject, CharSequence message, int type) {
		
		this.message = message;
		this.subject = subject;
		this.type    = type;
	}
	public GlobalMessage(CharSequence message, int type) {
		
		this.message = message;
		this.type    = type;
	}

	public static int getINFO() {
		
		return INFO;
	}

	public static int getWARN() {
		
		return WARN;
	}

	public static int getERROR() {
		
		return ERROR;
	}

	public static GlobalMessage create(CharSequence message) {
		
		return new GlobalMessage(message);
	}

	public static GlobalMessage create(CharSequence subject, CharSequence message) {
		
		return new GlobalMessage(subject, message);
	}

	public static GlobalMessage create(CharSequence subject, CharSequence message, int type) {
		
		return new GlobalMessage(subject, message, type);
	}

	public static GlobalMessage create(CharSequence message, int type) {
		
		return new GlobalMessage(message, type);
	}

	public CharSequence getSubject() {
		
		return subject;
	}
	
	public CharSequence getMessage() {
		
		return message;
	}
	
	public int getType() {
		
		return type;
	}
	
	public CharSequence getActionText() {
		
		return actionText;
	}
	
	public Runnable getAction() {
		
		return action;
	}
	
	public boolean isSnake() {
		
		return snake;
	}
	
	public long getDuration() {
		
		return duration;
	}
	
	public long getDelay() {
		
		return delay;
	}
	
	public int getPosition() {
		
		return position;
	}
	
	public int getRelationDegree() {
		
		return relationDegree;
	}
	
	public GlobalMessage mesage(CharSequence message) {
		
		this.message = message;
		return this;
	}
	
	public GlobalMessage subject(CharSequence subject) {
		
		this.subject = subject;
		return this;
	}
	
	public GlobalMessage actionText(CharSequence actionText) {
		
		this.actionText = actionText;
		return this;
	}
	
	public GlobalMessage type(int type) {
		
		this.type = type;
		return this;
	}
	
	public GlobalMessage position(int position) {
		
		this.position = position;
		return this;
	}
	
	public GlobalMessage action(Runnable action) {
		
		this.action = action;
		return this;
	}
	
	public GlobalMessage isSnake(boolean isSnake) {
		
		this.snake = isSnake;
		return this;
	}
	
	public GlobalMessage duration(long duration) {
		
		this.duration = duration;
		return this;
	}
	
	public GlobalMessage delay(long delay) {
		
		this.delay = delay;
		return this;
	}
	
	public GlobalMessage relationDegree(int degree) {
		
		relationDegree = degree;
		return this;
	}
	
	@IntDef({INFO, WARN, ERROR})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Type {}
}
