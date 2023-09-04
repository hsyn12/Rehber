package tr.xyz.kandom

import org.junit.Assert
import org.junit.Test

class KandomTest {
	
	@Test
	fun testNextBoolWhenPercentIsZeroOrLessThenReturnFalse() {
		// Arrange
		val percent = 0
		
		// Act
		val result = Kandom.nextBool(percent)
		
		// Assert
		Assert.assertFalse(result)
	}
	
	@Test
	fun testNextBoolWhenPercentIsHundredOrMoreThenReturnTrue() {
		// Arrange
		val percent = 100
		
		// Act
		val result = Kandom.nextBool(percent)
		
		// Assert
		Assert.assertTrue(result)
	}
	
	@Test
	fun testNextBoolWhenPercentIsBetweenOneAndNinetyNineThenReturnTrueOrFalse() {
		// Arrange
		val percent = 50
		
		// Act
		val result = Kandom.nextBool(percent)
		
		// Assert
		Assert.assertTrue(result == true || result == false)
	}
	
	@Test
	fun testNextIntWhenEndExclusiveIsSpecifiedThenReturnIntegerLessThanEndExclusive() {
		// Arrange
		val endExclusive = 10
		
		// Act
		val result = Kandom.nextInt(endExclusive)
		
		// Assert
		Assert.assertTrue(result < endExclusive)
	}
	
	@Test
	fun testNextIntWhenStartInclusiveAndEndExclusiveAreSpecifiedThenReturnIntegerWithinRange() {
		// Arrange
		val startInclusive = 5
		val endExclusive = 10
		
		// Act
		val result = Kandom.nextInt(startInclusive, endExclusive)
		
		// Assert
		Assert.assertTrue(result in startInclusive until endExclusive)
	}
	
	@Test
	fun testNextIntWhenNoParametersAreSpecifiedThenReturnIntegerLessThanMaxValue() {
		// Act
		val result = Kandom.nextInt()
		
		// Assert
		Assert.assertTrue(result < Int.MAX_VALUE)
	}
}