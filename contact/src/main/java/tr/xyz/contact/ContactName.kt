package tr.xyz.contact

@JvmInline
value class ContactName(private val _name: String?) {

	val name get() = _name ?: ""
	override fun toString(): String = name
}