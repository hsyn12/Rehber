package tr.xyz.contact

import com.tr.hsyn.perfectsort.PerfectSort
import tr.xyz.dict.DatBox

class Contact(
	val contactId: Long,
	val name: String?,
	val pic: String?) : DatBox() {
	
	override fun equals(other: Any?): Boolean = other is Contact && contactId == other.contactId
	
	override fun hashCode(): Int = contactId.hashCode()
	
	override fun toString(): String = "Contact($contactId, $name, $pic)"
	
	fun copy(name: String? = this.name, pic: String? = this.pic): Contact = Contact(contactId, name, pic)
	
	operator fun compareTo(other: Contact): Int = PerfectSort.compare(name, other.name)
	
	
}