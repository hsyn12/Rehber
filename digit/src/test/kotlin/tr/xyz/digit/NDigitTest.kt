package tr.xyz.digit

import org.junit.Assert.assertEquals
import org.junit.Test

class NDigitTest {
	
	@Test
	fun testNDigitSetValue() {
		val digitValue = 5
		val max = 5
		val min = 0
		
		val digit = NDigit(max, min, digitValue)
		digit.digitValue = 1
		val expected = min + 1
		assertEquals(expected, digit.digitValue)
	}
	
	@Test
	fun testNDigitPositiveOverflow() {
		val digit = Digit.newDigit(0, 5, 4)
		digit += 1
		val expected = 0
		assertEquals(expected, digit.digitValue)
	}
	
	@Test
	fun testNDigitNegativeOverflow() {
		val digit = Digit.newDigit(0, 5, 0)
		val digit2 = Digit.newDigit(0, 5, 1)
		val result = digit - digit2
		val expected = Digit.newDigit(4)
		assertEquals(expected, result)
	}
	
	@Test
	fun testNDigitTimes() {
		
		val row = Digit.newDigit(min = 0, max = 10, digitValue = 4)
		val col = Digit.newDigit(6)
		val anotherRow = Digit.newDigit(6)
		row.left = anotherRow
		row *= col
		println("row = $row")
		val expected = 4
		assertEquals(expected, row.digitValue)
	}
}