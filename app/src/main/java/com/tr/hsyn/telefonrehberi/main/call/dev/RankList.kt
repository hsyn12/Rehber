package com.tr.hsyn.telefonrehberi.main.call.dev

class RankList(val ranks: List<Rank>) {

	fun getRank(key: String): Rank? = ranks.find {it.key == key}
}