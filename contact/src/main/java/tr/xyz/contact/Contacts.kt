package tr.xyz.contact

class Contacts(val contacts: List<Contact>) {
	
	private val map: Map<Long, Contact> = contacts.associateBy(Contact::contactId)
	
	operator fun get(id: Long): Contact? = map[id]
	
	operator fun get(id: String): Contact? = map[id.toLong()]
	
	operator fun contains(id: Long): Boolean = map.containsKey(id)
}