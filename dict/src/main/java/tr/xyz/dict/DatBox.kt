@file:Suppress("UNCHECKED_CAST")

package tr.xyz.dict

import tr.xyz.kkey.Key

open class DatBox {
	
	private val dict: MutableMap<Key, Any> = hashMapOf()
	
	operator fun <V> set(key: Key, value: Any): V? = dict.put(key, value) as V?
	
	operator fun <V> get(key: Key): V? = dict[key] as V?
	
	/**
	 * Removes the data associated with the specified key.
	 *
	 * @param key the key
	 * @return previous value or `null`
	 */
	fun <V> removeData(key: Key): V? = dict.remove(key) as V?
	
	operator fun contains(key: Key): Boolean = dict.containsKey(key)
	
}