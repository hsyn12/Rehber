package tr.xyz.contact

/**
 * Contacts
 *
 * @property contacts List of contacts
 * @constructor Create new Contacts
 */
class Contacts(val contacts: List<Contact>) {
	
	/**
	 * Map object that associated contact ID with contact.
	 */
	private val map: Map<Long, Contact> = contacts.associateBy(Contact::contactId)
	
	/**
	 * Gets contact by ID
	 *
	 * @param id contact id
	 * @return contact or `null` if not found
	 */
	operator fun get(id: Long): Contact? = map[id]
	
	/**
	 * Gets contact by ID
	 *
	 * @param id contact id
	 * @return contact or `null` if not found
	 */
	operator fun get(id: String): Contact? = map[id.toLong()]
	
	/**
	 * Checks if contact exists.
	 *
	 * @param id contact id
	 * @return `true` if contact exists
	 */
	operator fun contains(id: Long): Boolean = map.containsKey(id)
}