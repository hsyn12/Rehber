@file:Suppress("UNCHECKED_CAST")

package tr.xyz.dict

import tr.xyz.kkey.Key

open class DatBox {
	
	private val dict: MutableMap<Key, Any> = hashMapOf()
	
	operator fun <V> set(key: Key, value: V): V? = dict.put(key, value as Any) as V?
	
	operator fun <V> get(key: Key): V? = dict[key] as V?
	
	fun getBoolData(key: Key): Boolean = dict[key] as Boolean? ?: false
	
	/**
	 * Removes the data associated with the specified key.
	 *
	 * @param key the key
	 * @return previous value or `null`
	 */
	fun <V> removeKey(key: Key): V? = dict.remove(key) as V?
	
	/**
	 * Checks if the map contains the specified key.
	 *
	 * @param key the key
	 * @return `true` if the map contains the specified key
	 */
	operator fun contains(key: Key): Boolean = dict.containsKey(key)
	
}