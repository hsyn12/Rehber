package tr.xyz.timek

import org.junit.Assert.fail
import org.junit.Test
import tr.xyz.timek.duration.TimeUnit

class TimeDurationsTest {
	
	@Test
	fun testValidateTimeDurationWhenValidDurationThenNoException() {
		try {
			TimeDurations.of("1:1:1:1:1:1:1")
		}
		catch (e: Exception) {
			fail("Exception should not be thrown for valid time duration")
		}
	}
	
	@Test(expected = IllegalArgumentException::class)
	fun testValidateTimeDurationWhenInvalidDurationThenException() {
		TimeDurations.of("100:100:100:100:100:100:1000")
	}
	
	@Test
	fun testValidateTimeDurationWithValidInputs() {
		try {
			TimeDurations.validateTimeDuration(59, TimeUnit.SECOND)
		}
		catch (e: Exception) {
			fail("Exception should not be thrown for valid inputs")
		}
	}
	
	@Test(expected = IllegalArgumentException::class)
	fun testValidateTimeDurationWithInvalidInputs() {
		TimeDurations.validateTimeDuration(60, TimeUnit.SECOND)
	}
	
	@Test
	fun testValidateTimeDurationWithBoundaryInputs() {
		try {
			TimeDurations.validateTimeDuration(0, TimeUnit.SECOND)
			TimeDurations.validateTimeDuration(59, TimeUnit.SECOND)
		}
		catch (e: Exception) {
			fail("Exception should not be thrown for boundary inputs")
		}
	}
}