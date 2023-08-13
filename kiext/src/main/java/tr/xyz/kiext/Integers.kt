package tr.xyz.kiext

val Int.isZero: Boolean get() = this == 0
val Int.isNotZero: Boolean get() = this != 0
val Long.isZero: Boolean get() = this == 0L
val Long.isNotZero: Boolean get() = this != 0L

