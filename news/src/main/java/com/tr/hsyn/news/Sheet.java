package com.tr.hsyn.news;


import com.tr.hsyn.codefinder.CodeFinder;
import com.tr.hsyn.codefinder.CodeLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Haber
 */
public class Sheet extends SheetInit {
	
	/**
	 * Haberin kaynağı
	 */
	private final CodeLocation from;
	/**
	 * Haberin konusu
	 */
	private final CharSequence subject;
	/**
	 * Haberin nesnesi
	 */
	private final Object       object;
	
	public Sheet() {
		
		this.from    = CodeFinder.getLocation(4);
		this.subject = "-";
		this.object  = null;
		setTime(System.currentTimeMillis());
	}
	
	public Sheet(int stackIndex) {
		
		this.from    = CodeFinder.getLocation(stackIndex);
		this.subject = "-";
		this.object  = null;
		setTime(System.currentTimeMillis());
	}
	
	public Sheet(Object object) {
		
		this.from    = CodeFinder.getLocation(4);
		this.subject = "-";
		this.object  = object;
		setTime(System.currentTimeMillis());
	}
	
	public Sheet(Object object, int stackIndex) {
		
		this.from    = CodeFinder.getLocation(stackIndex);
		this.subject = "-";
		this.object  = object;
		setTime(System.currentTimeMillis());
	}
	
	public Sheet(CharSequence subject, Object object) {
		
		this.from    = CodeFinder.getLocation(4);
		this.subject = subject;
		this.object  = object;
		setTime(System.currentTimeMillis());
	}
	
	public Sheet(CharSequence subject, Object object, int stackIndex) {
		
		this.from    = CodeFinder.getLocation(stackIndex);
		this.subject = subject;
		this.object  = object;
		setTime(System.currentTimeMillis());
	}
	
	public Sheet(CodeLocation from, CharSequence subject, Object object) {
		
		this.from    = from;
		this.subject = subject;
		this.object  = object;
		setTime(System.currentTimeMillis());
	}
	
	@NotNull
	public CodeLocation getFrom() {
		
		return from;
	}
	
	@NotNull
	public CharSequence getSubject() {
		
		return subject;
	}
	
	@Nullable
	public Object getObject() {
		
		return object;
	}
	
	@NotNull
	@Override
	public String toString() {
		
		return "Sheet{" +
		       "from=" + from +
		       ", subject=" + subject +
		       ", object=" + object +
		       ", labels=" + (getLabels() == null ? "[]" : getLabels()) +
		       '}';
	}
}
