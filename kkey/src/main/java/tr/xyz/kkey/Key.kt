package tr.xyz.kkey

/**
 * Key
 *
 * @property id ID of the key
 * @property name name of the key
 * @constructor Create new `Key`
 */
data class Key(val id: Int, val name: String) {
	
	//@off
	companion object {
		val INVALID                = Key(0,  "Invalid")                
		val CALL_LOG               = Key(2,  "Call Log")               
		val CONTACTS               = Key(3,  "Contacts")               
		val CALL_LOG_CALLS         = Key(4,  "Call Log Calls")         
		val MESSAGE                = Key(5,  "Message")                
		val CONTACT_LIST_UPDATED   = Key(6,  "Contact List Updated")   
		val NEW_CONTACTS           = Key(7,  "New Contacts")           
		val DELETED_CONTACTS       = Key(8,  "Deleted Contacts")       
		val SELECTED_CALL          = Key(9,  "Call Selected")          
		val SELECTED_CONTACT       = Key(10, "Contact Selected")       
		val RELATION_DEGREE        = Key(11, "Relation Degree")        
		val CONTEXT                = Key(12, "Context")                
		val SHOW_CALLS             = Key(13, "Show Calls")             
		val REFRESH_CALL_LOG       = Key(14, "Refresh CallLog")        
		val MOST_CALLS_FILTER_TYPE = Key(15, "Most Calls Filter Type")
		val CALL_LOG_FILTER        = Key(16, "CallLog Filter")         
		val CALL_STORY             = Key(17, "Call Story")             
		val REFRESH_CONTACTS       = Key(18, "Sign Refresh Contacts")  
		val CALL_LOG_UPDATED       = Key(20, "CallLog Updated")        
		val CONTACT_LOG            = Key(22, "Contact Log")            
		val CALL_LOG_LOADING       = Key(23, "CallLog Loading")        
		val CONTACTS_LOADING       = Key(24, "Contacts Loading")       
		
	} //@on
}








