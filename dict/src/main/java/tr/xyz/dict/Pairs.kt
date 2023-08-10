package tr.xyz.dict

interface Pairs<K, V> {
	
	fun put(key: K, value: V): V?
	
	fun get(key: K): V?
	
	fun remove(key: K): V?
	
	
}