@file:JvmName("Concurrent")

package tr.xyz.koncurrent

import kotlinx.coroutines.*
import tr.xyz.klog.warn

private val exceptionHandler = CoroutineExceptionHandler {_, e -> e.warn}

val contextMain = Dispatchers.Main
val contextIO = Dispatchers.IO
val contextDefault = Dispatchers.Default
val contextWorker = Dispatchers.Default

/**
 * This is the UI scope
 */
val mainScope = CoroutineScope(Job() + contextMain + exceptionHandler)
/**
 * This is the IO scope
 */
val ioScope = CoroutineScope(Job() + contextIO + exceptionHandler)
/**
 * This is the default background scope
 */
val defaultScope = CoroutineScope(Job() + contextDefault + exceptionHandler)
/**
 * This is the Worker scope
 */
val workerScope = CoroutineScope(Job() + contextWorker + exceptionHandler)

/**
 *  Runs the action on the main thread
 */
fun onMain(action: () -> Unit): Job = mainScope.launch {action()}

/**
 *  Runs the action on the IO thread
 */
fun onIO(action: () -> Unit): Job = ioScope.launch {action()}

/**
 *  Runs the action on the background thread
 */
fun onBackground(action: () -> Unit): Job = defaultScope.launch {action()}

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