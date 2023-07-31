package com.tr.hsyn.shower;


/**
 * Provides to manage a state of active and passive.
 */
public interface ShowTimer {
	
	/**
	 * Changes the state.
	 *
	 * @param showTime {@code true} for active, {@code false} for passive
	 */
	void showTime(boolean showTime);
	
	/**
	 * Returns the state.
	 *
	 * @return {@code true} if active, {@code false} if passive
	 */
	boolean isShowTime();
}
