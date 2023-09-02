package tr.xyz.bit

import org.junit.Assert.assertEquals
import org.junit.Test

class DigitTest {
	@Test
	fun testDigitValue() {
		var digit = Digit(-10, -10, 0)
		digit--
		assertEquals(0, digit.digitValue)
		
		digit.digitValue = 0
		digit++
		assertEquals(-10, digit.digitValue)
	}
	
	@Test
	fun testPositiveDigitValueIncreases() {
		var digit = Digit(0, 0, 5)
		assertEquals(0, digit.digitValue)
		digit++
		assertEquals(1, digit.digitValue)
		digit++
		assertEquals(2, digit.digitValue)
		digit++
		assertEquals(3, digit.digitValue)
		digit++
		assertEquals(4, digit.digitValue)
		digit++
		assertEquals(5, digit.digitValue)
		digit++
		assertEquals(0, digit.digitValue)
		digit++
		assertEquals(1, digit.digitValue)
		
	}
	
	@Test
	fun testPositiveDigitValueDecreases() {
		var digit = Digit(0, 0, 5)
		assertEquals(0, digit.digitValue)
		digit--
		assertEquals(5, digit.digitValue)
		digit--
		assertEquals(4, digit.digitValue)
		digit--
		assertEquals(3, digit.digitValue)
		digit--
		assertEquals(2, digit.digitValue)
		digit--
		assertEquals(1, digit.digitValue)
		digit--
		assertEquals(0, digit.digitValue)
		digit--
		assertEquals(5, digit.digitValue)
		
		
	}
	
	@Test
	fun testNegativeDigitValueIncreases() {
		var digit = Digit(-5, -5, 5)
		assertEquals(-5, digit.digitValue)
		digit++
		assertEquals(-4, digit.digitValue)
		digit++
		assertEquals(-3, digit.digitValue)
		digit++
		assertEquals(-2, digit.digitValue)
		digit++
		assertEquals(-1, digit.digitValue)
		digit++
		assertEquals(0, digit.digitValue)
		digit++
		assertEquals(1, digit.digitValue)
		digit++
		assertEquals(2, digit.digitValue)
		digit++
		assertEquals(3, digit.digitValue)
		digit++
		assertEquals(4, digit.digitValue)
		digit++
		assertEquals(5, digit.digitValue)
		digit++
		assertEquals(-5, digit.digitValue)
		digit++
		assertEquals(-4, digit.digitValue)
		
	}
	
	@Test
	fun testNegativeDigitValueDecreases() {
		var digit = Digit(-3, -5, 5)
		assertEquals(-3, digit.digitValue)
		digit--
		assertEquals(-4, digit.digitValue)
		digit--
		assertEquals(-5, digit.digitValue)
		digit--
		assertEquals(5, digit.digitValue)
		digit--
		assertEquals(4, digit.digitValue)
		digit--
		assertEquals(3, digit.digitValue)
		digit--
		assertEquals(2, digit.digitValue)
		digit--
		assertEquals(1, digit.digitValue)
		digit--
		assertEquals(0, digit.digitValue)
		
		
	}
}