package tr.xyz.timek

/**
 * Zaman birimlerinin milisaniye değerlerini tanımlar.
 * Bu değerler sabittir ve en kaba şekilde değerlendirilir.<br></br><br></br>
 *
 * Mesela 1 gün daima 24 saattir.<br></br>
 * Mesela 1 ay daima 30 gündür.<br></br>
 * Mesela 1 yıl daima 365 gündür.<br></br><br></br>
 *
 * Bu sebeple, bu değerler üzerinden ince hesap yapmak doğru değildir.
 *
 */
interface TimeMillis {
	companion object {
		const val SECOND = 1000L
		const val MINUTE = SECOND * 60
		const val HOUR = MINUTE * 60
		const val DAY = HOUR * 24
		const val MONTH = DAY * 30
		const val YEAR = DAY * 365
	}
}