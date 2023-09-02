package tr.xyz.digit

import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class DigitTest {
	@Test
	fun testSetValue() {
		
		val digit = Digit(0, 0, 5)
		
		digit.digitValue = 5
		assertEquals(5, digit.digitValue)
		assertEquals(0, digit.cycle)
		
		digit.digitValue = -1
		assertEquals(5, digit.digitValue)
		assertEquals(-1, digit.cycle)
		
		digit.digitValue = 6
		assertEquals(0, digit.digitValue)
		assertEquals(1, digit.cycle)
		
		digit.digitValue = -2
		assertEquals(4, digit.digitValue)
		assertEquals(-1, digit.cycle)
	}
	
	@Test
	fun testIncrement() {
		var digit = Digit()
		digit++
		assertEquals(1, digit.digitValue)
	}
	
	@Test
	fun testDecrement() {
		var digit = Digit(5)
		digit--
		assertEquals(4, digit.digitValue)
	}
	
	@Test
	fun testAddition() {
		val digit1 = Digit(3)
		val digit2 = Digit(5)
		val result = digit1 + digit2
		assertEquals(8, result.digitValue)
	}
	
	@Test
	fun testSubtraction() {
		val digit1 = Digit(8)
		val digit2 = Digit(2)
		assertEquals(8, digit1.digitValue)
		assertEquals(2, digit2.digitValue)
		
		val result = digit2 - digit1
		assertEquals(-6, result.digitValue)
		
		val result2 = digit1 - digit2
		assertEquals(6, result2.digitValue)
	}
	
	@Test
	fun testOverflow() {
		var digit = Digit(10, max = 10)
		digit++
		assertEquals(digit.min, digit.digitValue)
		assertEquals(1, digit.cycle)
	}
	
	@Test
	fun testUnderflow() {
		var digit = Digit(0, min = 0, max = 10)
		digit--
		assertEquals(10, digit.digitValue)
		assertEquals(-1, digit.cycle)
	}
	
	@Test
	fun testEquals() {
		val digit1 = Digit(5)
		val digit2 = Digit(5)
		val digit3 = Digit(3)
		
		assertEquals(digit1, digit2)
		assertNotEquals(digit1, digit3)
	}
	
	@Test
	fun testToString() {
		val digit = Digit(7)
		assertEquals("7", digit.toString())
	}
	
}