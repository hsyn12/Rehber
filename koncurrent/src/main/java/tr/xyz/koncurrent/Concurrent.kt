package tr.xyz.koncurrent

import kotlinx.coroutines.*

/**
 * This is the UI scope
 */
val mainScope = CoroutineScope(Job() + Dispatchers.Main)
/**
 * This is the IO scope
 */
val ioScope = CoroutineScope(Job() + Dispatchers.IO)
/**
 * This is the default background scope
 */
val defaultScope = CoroutineScope(Job() + Dispatchers.Default)
/**
 * This is the Worker scope
 */
val workerScope = CoroutineScope(Job() + Dispatchers.Default)

/**
 *  Runs the action on the main thread
 */
fun onMain(action: () -> Unit): Job = mainScope.launch { action() }

/**
 *  Runs the action on the IO thread
 */
fun onIO(action: () -> Unit): Job = ioScope.launch { action() }

/**
 *  Runs the action on the background thread
 */
fun onBackground(action: () -> Unit): Job = defaultScope.launch { action() }

/**
 *  Runs the action on the worker thread
 */
fun onWorker(action: () -> Unit): Job = workerScope.launch { action() }

/**
 *  Runs the action on the main thread
 */
fun (() -> Unit).runOnMain(): Job = onMain(this)

/**
 *  Runs the action on the IO thread
 */
fun (() -> Unit).runOnIO(): Job = onIO(this)

/**
 *  Runs the action on the background thread
 */
fun (() -> Unit).runOnBackground(): Job = onBackground(this)

/**
 *  Runs the action on the worker thread
 */
fun (() -> Unit).runOnWorker(): Job = onWorker(this)

suspend fun onMain(delay: Long = 0L, action: () -> Unit): Job {
	if (delay > 0) {
		delay(delay)
	}
	return onMain(action)
}

suspend fun onIO(delay: Long = 0L, action: () -> Unit): Job {
	if (delay > 0) {
		delay(delay)
	}
	return onIO(action)
}

suspend fun onBackground(delay: Long = 0L, action: () -> Unit): Job {
	if (delay > 0) {
		delay(delay)
	}
	return onBackground(action)
}

suspend fun onWorker(delay: Long = 0L, action: () -> Unit): Job {
	if (delay > 0) {
		delay(delay)
	}
	return onWorker(action)
}