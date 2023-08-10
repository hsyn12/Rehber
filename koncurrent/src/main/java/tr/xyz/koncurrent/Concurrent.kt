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

fun _newScope(
	context: CoroutineContext = contextDefault,
	name: String = "Routine",
	exceptionHandler: CoroutineExceptionHandler = _exceptionHandler
) = CoroutineScope(Job() + context + exceptionHandler + CoroutineName(name))

/**
 * New scope for the worker thread
 */
val newScope: CoroutineScope
	get() = _newScope(contextWorker)

/**
 * This is the UI scope
 */
val mainScope = _newScope(contextMain)
/**
 * This is the IO scope
 */
val ioScope = _newScope(contextIO)
/**
 * This is the default background scope
 */
val defaultScope = _newScope(contextDefault)
/**
 * This is the Worker scope
 */
val workerScope = _newScope(contextWorker)

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
fun Function0<Unit>.runOnMain(): Job = onMain(this)

/**
 *  Runs the action on the IO thread
 */
fun Function0<Unit>.runOnIO(): Job = onIO(this)

/**
 *  Runs the action on the background thread
 */
fun Function0<Unit>.runOnBackground(): Job = onBackground(this)

/**
 *  Extension for functions that no parameters and no return value
 *  to execute directly on the worker thread
 */
fun Function0<Unit>.runOnWorker(): Job = workerScope.launch {invoke()}

/**
 *  Extension for functions that have a parameter and no return value
 *  to execute directly on the worker thread
 */
fun <P> Function1<P, Unit>.runOnWorker(param: P): Job = workerScope.launch {invoke(param)}

/**
 *  Extension for functions that have two parameters and have no return value
 *  to execute directly on the worker thread
 */
fun <P1, P2> Function2<P1, P2, Unit>.runOnWorker(param1: P1, param2: P2): Job = workerScope.launch {invoke(param1, param2)}









