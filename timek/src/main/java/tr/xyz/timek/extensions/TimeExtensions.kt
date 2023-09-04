package tr.xyz.timek.extensions

import tr.xyz.timek.Time.Companion.DEFAULT_ZONE_OFFSET
import java.time.Instant
import java.time.LocalDateTime

val Long.localDateTime: LocalDateTime get() = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), DEFAULT_ZONE_OFFSET)
val LocalDateTime.milliseconds: Long get() = this.toEpochSecond(DEFAULT_ZONE_OFFSET) * 1000