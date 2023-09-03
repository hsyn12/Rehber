package tr.xyz.timek.duration

import org.junit.Assert.assertEquals
import org.junit.Test

class TimeDurationsTest {
	
	@Test
	fun testPlusOperatorWhenAddingTwoTimeDurationsThenResultIsCorrect() {
		// Arrange
		val timeDuration1 = TimeDurations("0:0:0:5:5:5:5")
		val timeDuration2 = TimeDurations("0:0:0:1:1:1:1")
		val expected = TimeDurations("0:0:0:6:6:6:6")
		
		// Act
		val result = timeDuration1 + timeDuration2
		
		// Assert
		assertEquals(expected, result)
	}
	
	@Test
	fun testMinusOperatorWhenAddingTwoTimeDurationsThenResultIsCorrect() {
		// Arrange
		val timeDuration1 = TimeDurations("0:0:0:5:5:5:5")
		val timeDuration2 = TimeDurations("0:0:0:1:1:1:1")
		val expected = TimeDurations("0:0:0:4:4:4:4")
		
		// Act
		val result = timeDuration1 - timeDuration2
		
		// Assert
		assertEquals(expected, result)
	}
	
	@Test
	fun testMinusOperatorWhenAddingTwoTimeDurationsForOverflowThenResultIsCorrect() {
		// Arrange
		val timeDuration1 = TimeDurations("0:0:0:2:2:2:2")
		val timeDuration2 = TimeDurations("0:0:0:1:1:1:1")
		val expected = TimeDurations("0:0:0:23:59:59:999")
		
		// Act
		val result = timeDuration2 - timeDuration1
		
		// Assert
		assertEquals(expected, result)
	}
	
	@Test
	fun testWithWhenTimeDurationIsNonZeroThenReturnsIncreasedTimeDurations() {
		// Arrange
		val timeDurations = TimeDurations("0:0:0:5:5:5:5")
		val timeDuration = TimeDuration.minutes(5)
		val expected = TimeDurations("0:0:0:5:10:5:5")
		
		// Act
		val result = timeDurations with timeDuration
		
		// Assert
		assertEquals(expected, result)
	}
	
	@Test
	fun testWithWhenTimeDurationIsZeroThenReturnsSameTimeDurations() {
		// Arrange
		val timeDurations = TimeDurations("0:0:0:5:5:5:5")
		val timeDuration = TimeDuration.minutes(0)
		val expected = TimeDurations("0:0:0:5:5:5:5")
		
		// Act
		val result = timeDurations with timeDuration
		
		// Assert
		assertEquals(expected, result)
	}
}