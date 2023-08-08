package tr.xyz.koncurrent

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

val mainScope = CoroutineScope(Job() + Dispatchers.Main)
val ioScope = CoroutineScope(Job() + Dispatchers.IO)
val defaultScope = CoroutineScope(Job() + Dispatchers.Default)

fun onMain(action: () -> Unit) = mainScope.launch { action() }
fun onIO(action: () -> Unit) = ioScope.launch { action() }
fun onBackground(action: () -> Unit) = defaultScope.launch { action() }