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

fun newScope(
	context: CoroutineContext = contextDefault,
	name: String = "Routine",
	exceptionHandler: CoroutineExceptionHandler = _exceptionHandler
) = CoroutineScope(Job() + context + exceptionHandler + CoroutineName(name))

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

fun newWorker(name: String = "Worker", action: suspend () -> Unit): Job = newScope(contextWorker, name).launch {action()}

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
 *  Extension to run the action on the worker thread
 */
fun (() -> Unit).runOnWorker(): Job = onWorker(this)

/**
 *  Extension for functions that no parameters and no return value
 *  to execute directly on the worker thread
 */
fun Function0<Unit>.goWorker(): Job = workerScope.launch {invoke()}

/**
 *  Extension for functions that no parameters and have a return value
 *  to execute directly on the worker thread
 */
suspend fun <R> Function0<R>.goAsync(): R = workerScope.async {invoke()}.await()

/**
 *  Extension for functions that have a parameter and no return value
 *  to execute directly on the worker thread
 */
fun <P> Function1<P, Unit>.goWorker(param: P): Job = workerScope.launch {invoke(param)}

/**
 *  Extension for functions that have two parameters and have no return value
 *  to execute directly on the worker thread
 */
fun <P1, P2> Function2<P1, P2, Unit>.goWorker(param1: P1, param2: P2): Job = workerScope.launch {invoke(param1, param2)}

/**
 *  Extension for functions that have a parameter and have return value
 *  to execute directly on the worker thread
 */
suspend fun <P, R> Function1<P, R>.goWorker(param: P): R = workerScope.async {invoke(param)}.await()

/**
 *  Extension for functions that have two parameters and have return value
 *  to execute directly on the worker thread
 */
suspend fun <P1, P2, R> Function2<P1, P2, R>.goWorker(param1: P1, param2: P2): R = workerScope.async {invoke(param1, param2)}.await()







