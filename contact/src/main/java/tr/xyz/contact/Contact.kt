package tr.xyz.contact

import com.tr.hsyn.perfectsort.PerfectSort
import tr.xyz.dict.DatBox

/**
 * Contact
 *
 * @property contactId contact id
 * @property name name
 * @property pic picture
 * @constructor Create new `Contact`
 */
class Contact(
	val contactId: Long,
	val name: String?,
	val pic: String?) : DatBox() {
	
	override fun equals(other: Any?): Boolean = other is Contact && contactId == other.contactId
	
	override fun hashCode(): Int = contactId.hashCode()
	
	override fun toString(): String = "Contact($contactId, $name, $pic)"
	
	fun copy(name: String? = this.name, pic: String? = this.pic): Contact = Contact(contactId, name, pic)
	
	/**
	 * Compares this object with the specified object for order.
	 *
	 * @param other the object to be compared
	 * @return a negative integer, zero or a positive integer as this object [name] is less than,
	 * equal to or greater than the specified object [name].
	 */
	operator fun compareTo(other: Contact): Int = PerfectSort.compare(name, other.name)
	
	
}