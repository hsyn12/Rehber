package com.tr.hsyn.codefinder;


import org.jetbrains.annotations.NotNull;

import java.util.Locale;


public class CodeFinder {
	
	/**
	 * String format with default locale.
	 *
	 * @param msg  message
	 * @param args Args
	 * @return Formatted string
	 */
	@NotNull
	public static String format(Object msg, Object... args) {
		
		return msg == null ? "null" : String.format(Locale.getDefault(), msg.toString(), args);
	}
	
	@NotNull
	public static String findPlace() {
		
		return findPlace(6);
	}
	
	@NotNull
	public static String findPlace(int index) {
		
		return formatAsLog(getLocation(index));
	}
	
	@NotNull
	public static CodeLocation getLocation() {
		
		return getLocation(6);
	}
	
	@NotNull
	public static CodeLocation getLocation(int index) {
		
		var traces = Thread.currentThread().getStackTrace();
		int size   = traces.length;
		
		while (index >= size) --index;
		
		var element = traces[index];
		
		String clasName   = element.getClassName();
		String methodName = element.getMethodName();
		int    lineNumber = element.getLineNumber();
		String fileName   = element.getFileName();
		
		clasName = clasName.substring(clasName.lastIndexOf(".") + 1);
		
		return new CodeLocation(fileName, clasName, methodName, lineNumber);
	}
	
	@NotNull
	public static String formatAsLog() {
		
		var location = getLocation();
		
		return format("%s.%s (%s:%d)",
		              location.getClassName(),
		              location.getMethodName(),
		              location.getFileName(),
		              location.getLineNumber());
	}
	
	@NotNull
	public static String formatAsLog(@NotNull CodeLocation location) {
		
		return format("%s.%s (%s:%d)",
		              location.getClassName(),
		              location.getMethodName(),
		              location.getFileName(),
		              location.getLineNumber());
	}
}