package tr.xyz.kbext

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Tests any object for null.
 *
 * @receiver any object
 */
val Any?.isNull: Boolean
	get() = this == null

/**
 * Tests any object for not null.
 *
 * @receiver any object
 */
val Any?.isNotNull: Boolean
	get() = this != null

/**
 * Tests any object for `not null` with smart cast.
 *
 * @return `true` if the object is null
 */
@OptIn(ExperimentalContracts::class)
fun Any?.isNotNull(): Boolean {
	
	contract {
		returns(true) implies (this@isNotNull != null)
	}
	
	return this != null
}

/**
 * Tests any object for true. If the object is null, returns false. If the object is boolean, returns boolean. If the object is not null, returns true.
 */
val Any?.isTrue: Boolean
	get() {
		return if (this is Boolean?) this != null && this == true
		else this != null
	}

/**
 * Tests any object for false. Returns `true` if the object is null. Returns `true` if the object is boolean with value `false`. Returns `false` on any other possibility.
 */
val Any?.isFalse: Boolean
	get() {
		if (this == null) return true
		if (this is Boolean) return !this
		return false
	}

/**
 * Tests any boolean for `true`.
 */
val Boolean.isTrue: Boolean
	get() = this

/**
 * Tests any boolean for `false`.
 */
val Boolean.isFalse: Boolean
	get() = !this

/**
 * Tests any object for `boolean`.
 */
val Any?.isBoolean: Boolean
	get() = this is Boolean

/**
 * Tests any object for `boolean` with smart cast.
 *
 * @return `true` if the object is Boolean
 */
@ExperimentalContracts
fun Any?.isBoolean(): Boolean {
	
	contract {
		returns(true) implies (this@isBoolean is Boolean)
	}
	
	return this is Boolean
}

/**
 * Tests any object for `not null`.
 *
 * @param action action to execute if the object is not null
 * @param T type of the object
 * @return if the object is boolean, returns boolean value. If the object is not boolean, returns `true` if not null, `false` otherwise.
 * @receiver any object
 */
@OptIn(ExperimentalContracts::class)
inline infix fun <T> T?.ifTrue(action: (T) -> Unit): Boolean {
	
	if (this.isNotNull()) {
		if (this.isBoolean()) {
			if (this) {
				action(this)
				return true
			}
		}
		else {
			action(this)
			return true
		}
	}
	
	return false
}

/**
 * Tests any object for `null`.
 *
 * @param action action to execute if the object is null or boolean is `false`
 * @return if the object is boolean, returns `true` if it has false value, `false` otherwise. If the object is not boolean, returns `true` if null, `false` otherwise.
 * @receiver any object
 */
inline infix fun Any?.ifFalse(action: () -> Unit): Boolean {
	
	if (this.isNull) {
		action()
		return true
	}
	
	return false
}

fun <R> Boolean.ifElse(actionTrue: () -> R, actionFalse: () -> R): R {
	
	return if (this) actionTrue()
	else actionFalse()
}

val Any?.bool: Boolean
	get() {
		if (this == null) return false
		if (this is Boolean) return this
		if (this is Int) return this != 0
		if (this is Long) return this != 0L
		return true
	}


