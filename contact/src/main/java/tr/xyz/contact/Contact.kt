package tr.xyz.contact

import com.tr.hsyn.perfectsort.PerfectSort
import tr.xyz.dict.DatBox

/**
 * Contact
 *
 * @constructor Create new `Contact`
 * @property contactId contact id
 * @property name name
 */
class Contact(val contactId: Long, val name: String?) : DatBox() {
	
	override fun equals(other: Any?): Boolean = other is Contact && contactId == other.contactId
	
	override fun hashCode(): Int = contactId.hashCode()
	
	override fun toString(): String = "Contact($contactId, $name)"
	
	/**
	 * Compares this object with the specified object by [name].
	 *
	 * @param other the object to be compared
	 * @return a negative integer, zero or a positive integer as this object [name] is less than,
	 *   equal to or greater than the specified object [name].
	 */
	operator fun compareTo(other: Contact): Int = PerfectSort.compare(name, other.name)
}
