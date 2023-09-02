package tr.xyz.keext

inline fun <T> T.sure(block: (T) -> T): T = block(this)
inline fun <T> T.sure(ifTrue: T, predicate: (T) -> Boolean): T = if (predicate(this)) ifTrue else this
inline fun <T> T.right(block: (T) -> Unit) = block(this)
