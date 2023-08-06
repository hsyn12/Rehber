package com.tr.hsyn.xlog;


import static com.tr.hsyn.codefinder.CodeFinder.findPlace;
import static com.tr.hsyn.codefinder.CodeFinder.format;

import com.tr.hsyn.codefinder.CodeFinder;

import java.util.function.Supplier;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Logger.
 */
public final class xlog {
	
	private static final String TAG    = "Rehber";
	public static final  Logger logger = Logger.getLogger(TAG);
	
	static {
		logger.setLevel(Level.ALL);
	}
	
	/**
	 * Verilen nesne tüm log mesajlarını alır.
	 *
	 * @param handler Log mesajlarını işleyecek olan nesne
	 */
	public static void addHandler(Handler handler) {
		
		logger.addHandler(handler);
		logger.setUseParentHandlers(false);
	}
	
	/**
	 * Info log.
	 *
	 * @param msg  message
	 * @param args Args
	 */
	public static void i(Object msg, Object... args) {
		
		logger.info(format(findPlace() + " " + msg, args));
	}
	
	/**
	 * Info log with thread info.
	 *
	 * @param msg  message
	 * @param args Args
	 */
	public static void ix(Object msg, Object... args) {
		
		logger.info(format(findPlace() + " " + msg + getThreadInfo(), args));
	}
	
	private static String getThreadInfo() {
		
		return " [on " + Thread.currentThread().getName() + "]";
	}
	
	/**
	 * Debug log.
	 *
	 * @param msg  message
	 * @param args Args
	 */
	public static void d(Object msg, Object... args) {
		
		logger.fine(CodeFinder.formatAsLog() + " " + format(msg, args));
	}
	
	/**
	 * Debug log.
	 *
	 * @param msg  Message function
	 * @param args Message args
	 */
	public static void d(Supplier<Object> msg, Object... args) {
		
		logger.fine(format(findPlace() + " " + (msg != null ? msg.get() : null), args));
	}
	
	/**
	 * Debug log with thread name.
	 *
	 * @param msg  message
	 * @param args Args
	 */
	public static void dx(Object msg, Object... args) {
		
		logger.fine(format(findPlace() + " " + msg + getThreadInfo(), args));
	}
	
	/**
	 * Warn log.
	 *
	 * @param msg  message
	 * @param args Args
	 */
	public static void w(Object msg, Object... args) {
		
		logger.warning(CodeFinder.formatAsLog() + " " + format(msg, args));
	}
	
	public static void w(Supplier<Object> msg, Object... args) {
		
		logger.warning(format(findPlace() + " " + (msg != null ? msg.get() : null), args));
	}
	
	/**
	 * A warning log with thread name.
	 *
	 * @param msg  message
	 * @param args Args
	 */
	public static void wx(Object msg, Object... args) {
		
		logger.warning(format(findPlace() + " " + msg + getThreadInfo(), args));
	}
	
	/**
	 * Error log.
	 *
	 * @param msg  message
	 * @param args Args
	 */
	public static void e(Object msg, Object... args) {
		
		logger.severe(format(findPlace() + " " + msg, args));
	}
	
	/**
	 * Error log with thread name.
	 *
	 * @param msg  message
	 * @param args Args
	 */
	public static void ex(Object msg, Object... args) {
		
		logger.severe(format(findPlace() + " " + msg + getThreadInfo(), args));
	}
	
	
}