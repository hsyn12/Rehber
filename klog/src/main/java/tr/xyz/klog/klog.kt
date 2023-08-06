package tr.xyz.klog

import com.tr.hsyn.xlog.xlog

val String.debug: Unit
	get() = xlog.d(this)

val String.info: Unit
	get() = xlog.i(this)

val String.warn: Unit
	get() = xlog.w(this)

val String.error: Unit
	get() = xlog.e(this)

val String.debugx: Unit
	get() = xlog.dx(this)

val String.infox: Unit
	get() = xlog.ix(this)

val String.warnx: Unit
	get() = xlog.wx(this)

val String.errorx: Unit
	get() = xlog.ex(this)


