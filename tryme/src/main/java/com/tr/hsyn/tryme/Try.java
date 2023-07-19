package com.tr.hsyn.tryme;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;


/**
 * Provides to ignore errors.<br><br>
 *
 * <pre>
 * public Contact getContact(@NotNull String key) {
 *    return Try.ignore(() -> mapIdToContact.get(Long.parseLong(key)));
 * }</pre><br>
 * <p>
 * if this code generates an exception, the exception will be ignored and returns {@code null}.<br>
 */
public class Try {
	
	/**
	 * Runs the given job and ignores errors.
	 *
	 * @param runnable job
	 */
	public static void ignore(@NotNull final Runnable runnable) {
		
		try {runnable.run();}
		catch (Exception ignore) {}
	}
	
	/**
	 * Runs the given job and ignores errors.<br><br>
	 *
	 * <pre>
	 * public Contact getContact(@NotNull String key) {
	 *    return Try.ignore(() -> mapIdToContact.get(Long.parseLong(key)));
	 * }</pre><br>
	 * <p>
	 * if this code generates an exception, the exception will be ignored and returns {@code null}.<br>
	 *
	 * @param callable job
	 * @param <R>      type of the result
	 * @return {@code R} result
	 */
	@Nullable
	public static <R> R ignore(@NotNull final Callable<R> callable) {
		
		try {return callable.call();}
		catch (Exception ignore) {}
		
		return null;
	}
}
