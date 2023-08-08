@file:JvmName("klog")

package tr.xyz.klog

import com.tr.hsyn.codefinder.CodeFinder
import com.tr.hsyn.xlog.xlog
import java.util.*
import kotlin.math.max

private const val placeFactor = 4

private fun decorateLog(log: String, msg: String): String {
	
	val size = max(log.length, msg.length)
	val decor = "-".repeat(size)
	
	return buildString {
		append(decor)
			.append("\n")
			.append(log)
			.append("\n")
			.append(msg)
			.append("\n")
			.append(decor)
			.append("\n")
	}
}

val Any?.debug: Unit
	get() = xlog.logger.fine(decorateLog(CodeFinder.formatAsLog(CodeFinder.getLocation(placeFactor)), this?.toString() ?: "null"))

val Any?.info: Unit
	get() = xlog.logger.info(decorateLog(CodeFinder.formatAsLog(CodeFinder.getLocation(placeFactor)), this?.toString() ?: "null"))

val Any?.warn: Unit
	get() = xlog.logger.warning(decorateLog(CodeFinder.formatAsLog(CodeFinder.getLocation(placeFactor)), this?.toString() ?: "null"))

val Any?.error: Unit
	get() = xlog.logger.severe(decorateLog(CodeFinder.formatAsLog(CodeFinder.getLocation(placeFactor)), this?.toString() ?: "null"))


