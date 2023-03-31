package com.tr.hsyn.executors;


/**
 * UI thread runner
 */
public interface MainExecutor extends ExExecutor {
	
	/**
	 * Holder of Main (system ui) Executor.
	 * You must set very first.
	 */
	Holder HOLDER = new Holder();
	
	/**
	 * This is default method to execute the command on main thread.
	 *
	 * @param command Runnable to execute
	 */
	static void run(Runnable command) {
		
		HOLDER.mainExecutor.execute(command);
	}
	
	/**
	 * This is default method to execute the command on main thread.
	 *
	 * @param command Runnable to execute
	 * @param delay   Delay in milliseconds
	 */
	static void run(Runnable command, long delay) {
		
		HOLDER.mainExecutor.execute(command, delay);
	}
	
	/**
	 * Holder of Main (system ui) Executor
	 */
	class Holder {
		
		private MainExecutor mainExecutor;
		
		public MainExecutor getMainExecutor() {
			return mainExecutor;
		}
		
		public void setMainExecutor(MainExecutor mainExecutor) {
			this.mainExecutor = mainExecutor;
		}
	}
}
