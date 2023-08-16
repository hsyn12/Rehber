package tr.xyz.contact

@JvmInline
value class ContactId(val id: Long) {

	val isInvalid: Boolean get() = id < 1
	val isValid get() = id > 0
	override fun toString(): String = "$id"
}
