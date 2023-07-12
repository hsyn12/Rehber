package com.tr.hsyn.gate;


import com.tr.hsyn.execution.Runny;

import org.jetbrains.annotations.NotNull;


/**
 * <h2>Delayed Runner</h2>
 * <p>
 * Provides to define a process that will be run after a specified time.
 */
public class DelayedRunner {
	
	/**
	 * Action that will be run after a specified time.
	 */
	protected Runnable action;
	/**
	 * Whether the action has been processed.
	 */
	protected boolean  done;
	/**
	 * Whether the action is on background.
	 */
	protected boolean  onBackground;
	
	/**
	 * Constructor
	 */
	public DelayedRunner() {}
	
	/**
	 * Constructor
	 *
	 * @param action action that will be run.
	 */
	public DelayedRunner(@NotNull Runnable action) {
		
		this.action = () -> {
			done = true;
			onStart();
			action.run();
			onComplete();
		};
	}
	
	/**
	 * Called when the action is started.
	 */
	protected void onStart() {
		
		//xlog.d("İşlem çalışmaya başladı");
	}
	
	/**
	 * Called when the action is completed.
	 */
	protected void onComplete() {
		
		//xlog.d("İşlem tamamlandı");
	}
	
	/**
	 * @return the action
	 */
	public Runnable getAction() {
		
		return action;
	}
	
	/**
	 * Sets the action.
	 *
	 * @param action action
	 * @return this
	 */
	public DelayedRunner setAction(@NotNull Runnable action) {
		
		this.action = () -> {
			done = true;
			onStart();
			action.run();
			onComplete();
		};
		
		return this;
	}
	
	/**
	 * @return whether the action is on background
	 */
	public boolean isOnBackground() {
		
		return onBackground;
	}
	
	/**
	 * @return whether the action is done
	 */
	public boolean isDone() {
		
		return done;
	}
	
	/**
	 * Sets whether the action is done
	 *
	 * @param done whether the action is done
	 */
	public void setDone(boolean done) {
		
		this.done = done;
	}
	
	/**
	 * Sets the action is on the background.
	 *
	 * @return this
	 */
	public DelayedRunner onBackground() {
		
		this.onBackground = true;
		return this;
	}
	
	/**
	 * Sets whether the action is on the background.
	 *
	 * @param onBackground whether the action is on the background
	 * @return this
	 */
	public DelayedRunner onBackground(boolean onBackground) {
		
		this.onBackground = onBackground;
		return this;
	}
	
	/**
	 * Executes the action.
	 *
	 * @param delay the delay in milliseconds to execute after.
	 */
	public void run(long delay) {
		
		Runny.run(action, !onBackground, delay);
	}
	
	/**
	 * Allows the action to be canceled before the time expires.
	 * If an action has been processed, it cannot be canceled.
	 * If the {@link #isDone()} method returns {@code false}, the cancellation is 99% successful.
	 */
	public void cancel() {
		
		Runny.cancel(action, onBackground);
	}
}
