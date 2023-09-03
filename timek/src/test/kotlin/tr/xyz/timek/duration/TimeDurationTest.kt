package tr.xyz.timek.duration

import org.junit.Assert
import org.junit.Test

class TimeDurationTest {
	
	@Test
	fun testIncWhenCalledThenValueIncreasesByOne() {
		// Arrange
		val initialDuration = TimeDuration.of(5, TimeUnit.MINUTE)
		val expected = TimeDuration minutes 6
		
		// Act
		initialDuration.inc()
		
		// Assert
		Assert.assertEquals(expected, initialDuration)
	}
	
	@Test
	fun testIncWhenValueAtMaxThenValueWrapsToZero() {
		// Arrange
		val initialDuration = TimeDuration.of(59, TimeUnit.MINUTE)
		
		// Act
		initialDuration.inc()
		
		// Assert
		Assert.assertEquals(0, initialDuration.value.digitValue)
	}
	
	@Test
	fun testDecWhenValueAtMinThenValueWrapsToMax() {
		// Arrange
		var initialDuration = TimeDuration.of(0, TimeUnit.MINUTE)
		
		// Act
		initialDuration--
		
		// Assert
		Assert.assertEquals(59, initialDuration.value.digitValue)
		Assert.assertEquals(TimeUnit.MINUTE, initialDuration.unit)
	}
	
	@Test
	fun testIncWhenCalledThenUnitRemainsSame() {
		// Arrange
		val initialDuration = TimeDuration.of(5, TimeUnit.MINUTE)
		
		// Act
		initialDuration.inc()
		
		// Assert
		Assert.assertEquals(TimeUnit.MINUTE, initialDuration.unit)
	}
	
	@Test
	fun testPlusWhenSameUnitThenCorrectValueAndUnit() {
		// Arrange
		val duration1 = TimeDuration.of(30, TimeUnit.MINUTE)
		val duration2 = TimeDuration.of(15, TimeUnit.MINUTE)
		val expected = TimeDuration.of(45, TimeUnit.MINUTE)
		
		// Act
		val result = duration1 + duration2
		
		// Assert
		Assert.assertEquals(expected, result)
	}
	
	@Test(expected = IllegalArgumentException::class)
	fun testPlusWhenDifferentUnitsThenIllegalArgumentException() {
		// Arrange
		val duration1 = TimeDuration.of(30, TimeUnit.MINUTE)
		val duration2 = TimeDuration.of(1, TimeUnit.HOUR)
		
		// Act
		val result = duration1 + duration2
	}
	
	@Test
	fun testPlusWhenOverflowThenCorrectValueAndUnit() {
		// Arrange
		val duration1 = TimeDuration.of(30, TimeUnit.MINUTE)
		val duration2 = TimeDuration.of(40, TimeUnit.MINUTE)
		val expected = TimeDuration.of(10, TimeUnit.MINUTE)
		
		// Act
		val result = duration1 + duration2
		
		// Assert
		Assert.assertEquals(expected, result)
	}
	
	@Test
	fun testMinusWhenSameTimeUnitThenCorrectSubtraction() {
		// Arrange
		val duration1 = TimeDuration.of(30, TimeUnit.MINUTE)
		val duration2 = TimeDuration.of(15, TimeUnit.MINUTE)
		val expected = TimeDuration.of(15, TimeUnit.MINUTE)
		
		// Act
		val result = duration1 - duration2
		
		// Assert
		Assert.assertEquals(expected, result)
	}
	
	@Test(expected = IllegalArgumentException::class)
	fun testMinusWhenDifferentTimeUnitThenIllegalArgumentException() {
		// Arrange
		val duration1 = TimeDuration.of(30, TimeUnit.MINUTE)
		val duration2 = TimeDuration.of(1, TimeUnit.HOUR)
		
		// Act
		val result = duration1 - duration2
	}
	
	@Test
	fun testMinusWhenOverflowThenCorrectValueAndUnit() {
		// Arrange
		val duration1 = TimeDuration.of(30, TimeUnit.MINUTE)
		val duration2 = TimeDuration.of(40, TimeUnit.MINUTE)
		val expected = TimeDuration.of(50, TimeUnit.MINUTE)
		
		// Act
		val result = duration1 - duration2
		
		// Assert
		Assert.assertEquals(expected, result)
	}
	
	@Test
	fun testPlusAssignWhenUnitsAreSameThenValueIsSum() {
		// Arrange
		val duration1 = TimeDuration.of(30, TimeUnit.MINUTE)
		val duration2 = TimeDuration.of(15, TimeUnit.MINUTE)
		val expected = TimeDuration.of(45, TimeUnit.MINUTE)
		
		// Act
		duration1 += duration2
		
		// Assert
		Assert.assertEquals(expected, duration1)
	}
	
	@Test(expected = IllegalArgumentException::class)
	fun testPlusAssignWhenUnitsAreDifferentThenThrowsIllegalArgumentException() {
		// Arrange
		val duration1 = TimeDuration.of(30, TimeUnit.MINUTE)
		val duration2 = TimeDuration.of(1, TimeUnit.HOUR)
		
		// Act
		duration1 += duration2
	}
	
	@Test
	fun testPlusAssignWhenSumExceedsUnitLimitThenValueIsTruncated() {
		// Arrange
		val duration1 = TimeDuration.of(30, TimeUnit.MINUTE)
		val duration2 = TimeDuration.of(40, TimeUnit.MINUTE)
		val expected = TimeDuration.of(10, TimeUnit.MINUTE)
		
		// Act
		duration1 += duration2
		
		// Assert
		Assert.assertEquals(expected, duration1)
		Assert.assertEquals(1, duration1.value.cycle)
	}
	
	@Test
	fun testMinusAssignWhenSameUnitThenValueUpdated() {
		// Arrange
		val duration1 = TimeDuration.of(30, TimeUnit.MINUTE)
		val duration2 = TimeDuration.of(15, TimeUnit.MINUTE)
		val expected = TimeDuration.of(15, TimeUnit.MINUTE)
		
		// Act
		duration1 -= duration2
		
		// Assert
		Assert.assertEquals(expected, duration1)
	}
	
	@Test(expected = IllegalArgumentException::class)
	fun testMinusAssignWhenDifferentUnitsThenExceptionThrown() {
		// Arrange
		val duration1 = TimeDuration.of(30, TimeUnit.MINUTE)
		val duration2 = TimeDuration.of(1, TimeUnit.HOUR)
		
		// Act
		duration1 -= duration2
	}
	
	@Test
	fun testMinusAssignWhenNegativeResultThenValueUpdatedWithOverflow() {
		// Arrange
		val duration1 = TimeDuration.of(30, TimeUnit.MINUTE)
		val duration2 = TimeDuration.of(40, TimeUnit.MINUTE)
		val expected = TimeDuration.of(50, TimeUnit.MINUTE)
		
		// Act
		duration1 -= duration2
		
		// Assert
		Assert.assertEquals(expected, duration1)
		Assert.assertEquals(-1, duration1.value.cycle)
	}
	
	@Test
	fun testWithMethodWhenTwoTimeDurationsThenReturnsTimeDurations() {
		// Arrange
		val duration1 = TimeDuration.of(30, TimeUnit.MINUTE)
		val duration2 = TimeDuration.of(1, TimeUnit.HOUR)
		
		// Act
		val result = duration1 with duration2
		
		// Assert
		Assert.assertTrue(result.durations.contains(duration1))
		Assert.assertTrue(result.durations.contains(duration2))
	}
	
	@Test
	fun testFormatMillisecondsWithDifferentInputs() {
		// Arrange
		val inputOutputPairs = mapOf(
			0L to "00:00",
			60000L to "01:00",
			3600000L to "01:00:00",
			86400000L to "01:00:00:00",
			90061000L to "01:01:01:01",
		)
		
		// Act and Assert
		for ((input, expectedOutput) in inputOutputPairs) {
			val actualOutput = TimeDuration.formatMilliseconds(input)
			Assert.assertEquals(expectedOutput, actualOutput)
		}
	}
	
	@Test
	fun testFormatMillisecondsWithZeroMilliseconds() {
		// Arrange
		val milliseconds = 0L
		val expected = "00:00"
		
		// Act
		val actual = TimeDuration.formatMilliseconds(milliseconds)
		
		// Assert
		Assert.assertEquals(expected, actual)
	}
	
	@Test(expected = IllegalArgumentException::class)
	fun testFormatMillisecondsWithNegativeMilliseconds() {
		// Arrange
		val milliseconds = -1L
		
		// Act
		val actual = TimeDuration.formatMilliseconds(milliseconds)
	}
}