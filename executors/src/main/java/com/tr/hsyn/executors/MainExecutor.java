package com.tr.hsyn.executors;


/**
 * UI thread runner
 */
public interface MainExecutor extends ExExecutor {
	
	/**
	 * Holder of the Main (system ui) Executor.
	 * Must be set first.
	 */
	Holder HOLDER = new Holder();
	
	/**
	 * Executes the command on the main thread.
	 *
	 * @param command Runnable to execute
	 */
	static void run(Runnable command) {
		
		HOLDER.mainExecutor.execute(command);
	}
	
	/**
	 * Executes the command on the main thread.
	 *
	 * @param command Runnable to execute
	 * @param delay   Delay in milliseconds
	 */
	static void run(Runnable command, long delay) {
		
		HOLDER.mainExecutor.execute(command, delay);
	}
	
	/**
	 * Holder of Main the (system ui) Executor
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
