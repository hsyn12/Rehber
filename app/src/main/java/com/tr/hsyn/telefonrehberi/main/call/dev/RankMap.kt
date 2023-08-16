package com.tr.hsyn.telefonrehberi.main.call.dev

import tr.xyz.contact.Contact

class RankMap(val map: Map<Int, List<Rank>>) {

	val rankCount get() = map.values.asSequence().flatten().count()
	val sortedRankList get() = map.values.asSequence().flatten().sortedWith(Comparator.comparingInt(Rank::rank)).toList()
	operator fun get(rank: Int) = map[rank]
	operator fun get(contact: Contact): Int = map.entries.find {it.value[0].key == contact.id.toString()}?.value?.first()?.rank ?: 0
	operator fun get(rank: Int, contact: Contact): Rank? = map[rank]?.find {it.key == contact.id.toString()}
}