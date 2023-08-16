package tr.xyz.contact

import com.tr.hsyn.perfectsort.PerfectSort
import tr.xyz.dict.DatBox

/**
 * Contact
 *
 * @constructor Create new `Contact`
 * @property contactId contact id
 * @property contactName name
 */
class Contact(contactId: Long, _name: String?) : DatBox() {

	val id: Long = contactId
	val name: String = _name ?: ""
	val contactId: ContactId = ContactId(contactId)
	val contactName: ContactName = ContactName(_name)

	constructor(contactId: Long) : this(contactId, null)

	override fun equals(other: Any?): Boolean = other is Contact && id == other.id
	override fun hashCode(): Int = id.hashCode()
	override fun toString(): String = "Contact($id, $name)"

	/**
	 * Compares this object with the specified object by [contactName].
	 *
	 * @param other the object to be compared
	 * @return a negative integer, zero or a positive integer as this object
	 *     [name] is less than, equal to or greater than the specified object
	 *     [name].
	 */
	operator fun compareTo(other: Contact): Int = PerfectSort.compare(name, other.name)
}
