@file:JvmName("Concurrent")

package tr.xyz.koncurrent

import kotlinx.coroutines.*
import tr.xyz.klog.warn
import kotlin.coroutines.CoroutineContext

private val _exceptionHandler = CoroutineExceptionHandler {_, e -> e.warn}
private val contextMain = Dispatchers.Main
private val contextIO = Dispatchers.IO
private val contextDefault = Dispatchers.Default
private val contextWorker = Dispatchers.Default

fun newScope(context: CoroutineContext = contextDefault, name: String = "Routine", exceptionHandler: CoroutineExceptionHandler = _exceptionHandler) = CoroutineScope(Job() + context + exceptionHandler + CoroutineName(name))

val newWorkerScope: CoroutineScope
	get() = newScope(contextWorker)

/**
 * This is the UI scope
 */
val mainScope = newScope(contextMain)
/**
 * This is the IO scope
 */
val ioScope = newScope(contextIO)
/**
 * This is the default background scope
 */
val defaultScope = newScope(contextDefault)
/**
 * This is the Worker scope
 */
val workerScope = newScope(contextWorker)

/**
 *  Runs the action on the main thread
 */
fun onMain(action: suspend () -> Unit): Job = mainScope.launch {action()}

/**
 *  Runs the action on the IO thread
 */
fun onIO(action: suspend () -> Unit): Job = ioScope.launch {action()}

/**
 *  Runs the action on the background thread
 */
fun onBackground(action: suspend () -> Unit): Job = defaultScope.launch {action()}

/**
 *  Runs the action on the worker thread
 */
fun onWorker(action: suspend () -> Unit): Job = workerScope.launch {action()}

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
