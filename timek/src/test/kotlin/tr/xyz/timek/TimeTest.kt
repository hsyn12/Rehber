package tr.xyz.timek

import org.junit.Assert.assertEquals
import org.junit.Test
import tr.xyz.timek.duration.TimeUnit
import tr.xyz.timek.extensions.milliseconds
import java.time.LocalDateTime

class TimeTest {
	
	@Test
	fun testHowLongBeforeWhenOneYearBeforeThenReturnOneYear() {
		val oneYearBefore = LocalDateTime.now().minusYears(1).toEpochSecond(Time.DEFAULT_ZONE_OFFSET) * 1000
		val result = Time.howLongBefore(oneYearBefore)
		assertEquals(1, result.value)
		assertEquals(TimeUnit.YEAR, result.unit)
	}
	
	@Test
	fun testHowLongBeforeWhenOneMonthBeforeThenReturnOneMonth() {
		val oneMonthBefore = LocalDateTime.now().minusMonths(1).toEpochSecond(Time.DEFAULT_ZONE_OFFSET) * 1000
		val result = Time.howLongBefore(oneMonthBefore)
		assertEquals(1, result.value)
		assertEquals(TimeUnit.MONTH, result.unit)
	}
	
	@Test
	fun testHowLongBeforeWhenOneDayBeforeThenReturnOneDay() {
		val oneDayBefore = LocalDateTime.now().minusDays(1).milliseconds
		val result = Time.howLongBefore(oneDayBefore)
		assertEquals(1, result.value)
		assertEquals(TimeUnit.DAY, result.unit)
	}
	
	@Test
	fun testHowLongBeforeWhenOneHourBeforeThenReturnOneHour() {
		val oneHourBefore = LocalDateTime.now().minusHours(1).toEpochSecond(Time.DEFAULT_ZONE_OFFSET) * 1000
		val result = Time.howLongBefore(oneHourBefore)
		assertEquals(1, result.value)
		assertEquals(TimeUnit.HOUR, result.unit)
	}
	
	@Test
	fun testHowLongBeforeWhenOneMinuteBeforeThenReturnOneMinute() {
		val oneMinuteBefore = LocalDateTime.now().minusMinutes(1).toEpochSecond(Time.DEFAULT_ZONE_OFFSET) * 1000
		val result = Time.howLongBefore(oneMinuteBefore)
		assertEquals(1, result.value)
		assertEquals(TimeUnit.MINUTE, result.unit)
	}
}