@file:Suppress("UNCHECKED_CAST")

package tr.xyz.dict

import tr.xyz.kkey.Key

open class Dict<V> {
	
	private val dict: MutableMap<Key, Any> = hashMapOf()
	val keys: Set<Key> get() = dict.keys
	val values: Collection<Any> get() = dict.values
	
	fun put(key: Key, value: Any): V? = dict.put(key, value) as V?
	
	fun get(key: Key): V? = dict[key] as V?
	
	fun remove(key: Key): V? = dict.remove(key) as V?
	
	
}